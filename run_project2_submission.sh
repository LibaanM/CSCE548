#!/usr/bin/env bash
# Project 2: one-shot submission runner (tests + screenshot pauses).
#
# What it does:
#  - Compiles
#  - Runs DataLayerTester (data layer)
#  - Runs BusinessLayerTester (business layer)
#  - Starts LibraryServer on a free port (service layer)
#  - Runs LibraryClient against that port (service layer CRUD)
#  - Dumps a sample "data retrieval" call (GET /api/categories)
#  - Writes logs to .project2-submission-logs/
#
# You still need to TAKE SCREENSHOTS when prompted.
#
# Usage:
#   cd /Users/libaanmohamed/CSCE548
#   export DB_PASSWORD=postgres   # set your DB password
#   ./run_project2_submission.sh
#
# If you want a specific port range:
#   export PORT_BASE=7000
#
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT_DIR"

LOG_DIR="$ROOT_DIR/.project2-submission-logs"
mkdir -p "$LOG_DIR"

timestamp() { date +"%Y%m%d_%H%M%S"; }

say() { printf "\n%s\n" "$*"; }

pause_for_screenshot() {
  say ">>> Screenshot now (then press Enter to continue)"
  read -r _
}

choose_port() {
  local base="${PORT_BASE:-7000}"
  python3 - <<'PY'
import os, socket, sys
base = int(os.environ.get("PORT_BASE", "7000"))
for p in range(base, base + 11):
    s = socket.socket()
    try:
        s.bind(("127.0.0.1", p))
        print(p)
        sys.exit(0)
    except OSError:
        pass
    finally:
        try: s.close()
        except Exception: pass
print(-1)
sys.exit(1)
PY
}

wait_for_http_200() {
  local url="$1"
  local attempts="${2:-40}"
  for _ in $(seq 1 "$attempts"); do
    if command -v curl >/dev/null 2>&1; then
      code="$(curl -s -o /dev/null -w "%{http_code}" "$url" || true)"
      if [[ "$code" == "200" ]]; then
        return 0
      fi
    fi
    sleep 1
  done
  return 1
}

say "=== Project 2 Submission Runner ==="

if [[ -z "${DB_PASSWORD:-}" ]]; then
  say "NOTE: DB_PASSWORD is not set. If your PostgreSQL user requires a password, set it first:"
  say "  export DB_PASSWORD=postgres"
fi

RUN_TS="$(timestamp)"

say "Step 0: Compile"
mvn -q compile | tee "$LOG_DIR/${RUN_TS}_00_compile.log"
say "Log: $LOG_DIR/${RUN_TS}_00_compile.log"

say "Step 1: Data layer test (DAOs)"
mvn -q exec:java -Dexec.mainClass=edu.csce548.library.DataLayerTester | tee "$LOG_DIR/${RUN_TS}_01_data_layer.log"
say "Log: $LOG_DIR/${RUN_TS}_01_data_layer.log"
pause_for_screenshot

say "Step 2: Business layer test"
mvn -q exec:java -Dexec.mainClass=edu.csce548.library.BusinessLayerTester | tee "$LOG_DIR/${RUN_TS}_02_business_layer.log"
say "Log: $LOG_DIR/${RUN_TS}_02_business_layer.log"
pause_for_screenshot

say "Step 3: Start service layer (REST API server) in a new terminal tab/window"
PORT_CHOSEN="$(choose_port)"
if [[ "$PORT_CHOSEN" == "-1" ]]; then
  say "ERROR: Could not find a free port in range PORT_BASE..PORT_BASE+10"
  exit 1
fi

BASE_URL="http://localhost:$PORT_CHOSEN"
SERVER_LAUNCHER="$LOG_DIR/${RUN_TS}_03_server_launcher.sh"

# Write a launcher script so the new terminal can run the server with same env (PORT, DB_*)
{
  echo "#!/usr/bin/env bash"
  echo "cd \"$ROOT_DIR\""
  echo "export PORT=$PORT_CHOSEN"
  [[ -n "${DB_HOST:-}" ]] && echo "export DB_HOST=\"$DB_HOST\""
  [[ -n "${DB_NAME:-}" ]] && echo "export DB_NAME=\"$DB_NAME\""
  [[ -n "${DB_USER:-}" ]] && echo "export DB_USER=\"$DB_USER\""
  [[ -n "${DB_PASSWORD:-}" ]] && echo "export DB_PASSWORD=\"$DB_PASSWORD\""
  [[ -n "${DB_PORT:-}" ]] && echo "export DB_PORT=\"$DB_PORT\""
  echo "echo \"Library API server starting on http://localhost:\$PORT ...\""
  echo "exec mvn -q exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer"
} > "$SERVER_LAUNCHER"
chmod 700 "$SERVER_LAUNCHER"

open_server_in_new_terminal() {
  # macOS: open a new Terminal window running the server
  if [[ "$(uname)" == "Darwin" ]]; then
    osascript -e "tell application \"Terminal\" to do script \"$SERVER_LAUNCHER\"" 2>/dev/null && return 0
    # iTerm2
    osascript -e "tell application \"iTerm\" to create window with default profile command \"$SERVER_LAUNCHER\"" 2>/dev/null && return 0
  fi
  return 1
}

if open_server_in_new_terminal; then
  say "Opened a new Terminal window with the server. It will listen on: $BASE_URL"
else
  say "Could not open a new terminal automatically. Do this manually:"
  say "  1. Open a NEW terminal tab or window"
  say "  2. Run: $SERVER_LAUNCHER"
  say "  Or: cd \"$ROOT_DIR\" && PORT=$PORT_CHOSEN ./run_project2_server.sh"
  say ""
  say "Press Enter when the server is running (you should see 'Library API server running at ...')"
  read -r _
fi

say "Waiting for server to respond (GET /api/categories)..."
if ! wait_for_http_200 "$BASE_URL/api/categories" 40; then
  say "ERROR: Server did not become ready. Start it in another tab: $SERVER_LAUNCHER"
  exit 1
fi

say "Server is up at $BASE_URL"
pause_for_screenshot

say "Step 4: Service layer test (console client CRUD)"
BASE_URL="$BASE_URL" mvn -q exec:java -Dexec.mainClass=edu.csce548.library.client.LibraryClient | tee "$LOG_DIR/${RUN_TS}_04_service_client.log"
say "Log: $LOG_DIR/${RUN_TS}_04_service_client.log"
pause_for_screenshot

say "Step 5: Data retrieval proof (API JSON output)"
if command -v curl >/dev/null 2>&1; then
  curl -s "$BASE_URL/api/categories" | tee "$LOG_DIR/${RUN_TS}_05_api_categories.json" >/dev/null
  say "Saved: $LOG_DIR/${RUN_TS}_05_api_categories.json"
  say "Tip: open this file or re-run: curl $BASE_URL/api/categories"
else
  say "curl not found; skipping API JSON dump."
fi
pause_for_screenshot

say "DONE."
say "All logs are in: $LOG_DIR"
say "If you need DB-side proof too, run:"
say "  psql -U <user> -d library_management -f database/inspect_data.sql"
# Remove launcher script (may contain DB_PASSWORD)
rm -f "$SERVER_LAUNCHER" 2>/dev/null || true

