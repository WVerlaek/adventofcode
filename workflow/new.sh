#!/bin/bash
## Generates a new day (solution + unit test).
## Usage: ./new.sh <day> [<year>]

set -Eeuo pipefail

CURRENT_YEAR="25"

export DAY="${1:?"Usage: ./new.sh <day> [<year>]"}"
export YEAR="${2:-"$CURRENT_YEAR"}"

DAY_REGEX="^[0-9]{1,2}$"
YEAR_REGEX="^[0-9]{2}$"
if ! [[ "$DAY" =~ $DAY_REGEX ]]; then
    echo "Invalid day format: $DAY"
    exit 1
fi
if ! [[ "$YEAR" =~ $YEAR_REGEX ]]; then
    echo "Invalid year format: $YEAR, must be e.g. '$CURRENT_YEAR'"
    exit 1
fi

ROOT_DIR="$(dirname -- "$0")/.."
SRC_DIR="${ROOT_DIR}/src/main/kotlin/y${YEAR}"
mkdir -p "$SRC_DIR"
SRC_FILE="${SRC_DIR}/Day${DAY}.kt"
TEST_DIR="${ROOT_DIR}/src/test/kotlin/y${YEAR}"
mkdir -p "$TEST_DIR"
TEST_FILE="${TEST_DIR}/Day${DAY}Test.kt"

SRC_TEMPLATE="${ROOT_DIR}/workflow/template/source.template"
TEST_TEMPLATE="${ROOT_DIR}/workflow/template/test.template"

if [[ -f "$SRC_FILE" ]]; then
    echo "Source file already exists: $SRC_FILE"
    exit 1
fi
if [[ -f "$TEST_FILE" ]]; then
    echo "Test file already exists: $TEST_FILE"
    exit 1
fi

echo "Generating source and test file for year $YEAR day $DAY..."
envsubst < "$SRC_TEMPLATE" > "$SRC_FILE"
envsubst < "$TEST_TEMPLATE" > "$TEST_FILE"

echo "Opening files"
gp open "$SRC_FILE" || echo "Failed to open $SRC_FILE"
gp open "$TEST_FILE" || echo "Failed to open $TEST_FILE"
