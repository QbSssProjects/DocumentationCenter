#!/usr/bin/env python3
"""
Script do pobrania highlight.js z CDN i umieszczenia lokalnie
"""

import os
import urllib.request
import json

# Konfiguracja
BASE_URL = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0"
LOCAL_DIR = "F:\\projekty\\Praca\\documentation\\docsCenterGradleGenerated\\src\\main\\resources\\static\\js\\highlight"
CSS_THEME = "github-dark"

# Tworzenie katalogów
os.makedirs(f"{LOCAL_DIR}\\styles", exist_ok=True)
os.makedirs(f"{LOCAL_DIR}\\languages", exist_ok=True)

def download_file(url, local_path):
    """Pobiera plik z URL"""
    try:
        print(f"Pobieranie: {url}")
        urllib.request.urlretrieve(url, local_path)
        print(f"✅ Zapisano: {local_path}")
        return True
    except Exception as e:
        print(f"❌ Błąd: {e}")
        return False

# 1. Pobierz główny plik highlight.js
print("\n═══════════════════════════════════════════════════════════")
print("1. POBIERANIE GŁÓWNEGO HIGHLIGHT.JS")
print("═══════════════════════════════════════════════════════════\n")
download_file(
    f"{BASE_URL}/highlight.min.js",
    f"{LOCAL_DIR}\\highlight.min.js"
)

# 2. Pobierz CSS theme
print("\n═══════════════════════════════════════════════════════════")
print("2. POBIERANIE CSS THEME")
print("═══════════════════════════════════════════════════════════\n")
download_file(
    f"{BASE_URL}/styles/{CSS_THEME}.min.css",
    f"{LOCAL_DIR}\\styles\\{CSS_THEME}.min.css"
)

# 3. Lista wszystkich dostępnych języków
print("\n═══════════════════════════════════════════════════════════")
print("3. POBIERANIE JĘZYKÓW")
print("═══════════════════════════════════════════════════════════\n")

languages = [
    "bash", "c", "csharp", "cpp", "css", "dockerfile", "go", "java", "javascript",
    "json", "kotlin", "lua", "markdown", "perl", "php", "python", "ruby", "rust",
    "scala", "shell", "sql", "swift", "typescript", "xml", "yaml", "html", "xml",
    "plaintext", "diff", "gradle", "groovy", "java", "javascript", "kotlin",
    "lisp", "lua", "makefile", "markdown", "nginx", "perl", "php", "plaintext",
    "powershell", "python", "r", "ruby", "rust", "sass", "scala", "scheme", "shell",
    "sql", "swift", "tcl", "typescript", "vb", "vbnet", "vim", "xml", "yaml",
    "zsh", "assembly", "asm", "actionscript", "ada", "apache", "applescript",
    "arduino", "arm", "asp", "autohotkey", "autoit", "avrasm", "awk", "axapta",
    "basic", "bnf", "brainfuck", "cal", "capnproto", "ceylon", "clojure", "cmake",
    "cobol", "coffeescript", "coq", "cos", "Crystal", "cuda", "dart", "delphi",
    "django", "dns", "dos", "dsconfig", "dts", "dust", "ebnf", "eiffel", "elixir",
    "elm", "erb", "erlang", "excel", "fsharp", "gams", "gauss", "gcode", "gherkin",
    "glsl", "gml", "gauss", "haskell", "haxe", "hsp", "hy", "ibm", "icon", "inform7",
    "irpf90", "isbl", "jboss-cli", "jinja", "jorendorff", "jq", "jvm", "kconfig",
    "lasso", "ldif", "leaf", "less", "llvm", "lsl", "max", "mel", "mercury",
    "metapost", "mirc", "mma", "mmix", "mojo", "mojolicious", "monkey", "moonscript",
    "nanorc", "nasm", "neon", "nerv", "nix", "nsis", "nunjucks", "obj-c", "ocaml",
    "ogdl", "oocss", "opal", "openscad", "oxygene", "oz", "parser3", "pf", "pgsql",
    "pony", "processing", "profile", "prolog", "properties", "protobuf", "puppet",
    "purebasic", "purs", "qml", "qore", "rego", "rib", "riego", "ripple", "riscv",
    "roboconf", "routeros", "rsl", "ruleslanguage", "sas", "sass", "scad", "scheme",
    "scilab", "scss", "shader", "shell-session", "smali", "smalltalk", "sml",
    "sqf", "stan", "stata", "step21", "stylus", "subunit", "supercollider", "svelte",
    "svg", "swift", "swig", "systemverilog", "tadsfriction", "tap", "tcl", "terraform",
    "tex", "thrift", "toml", "twig", "two-way-binding", "typescript", "unicorn",
    "vala", "vbnet", "velocity", "verilog", "vhdl", "vim", "wast", "wren", "x86asm",
    "xl", "xquery", "yaml", "zephir", "zsh"
]

# Unikalne języki
languages = sorted(list(set(languages)))

success_count = 0
failed_count = 0

for lang in languages:
    url = f"{BASE_URL}/languages/{lang}.min.js"
    local_path = f"{LOCAL_DIR}\\languages\\{lang}.min.js"

    if download_file(url, local_path):
        success_count += 1
    else:
        failed_count += 1

print(f"\n═══════════════════════════════════════════════════════════")
print(f"PODSUMOWANIE")
print(f"═══════════════════════════════════════════════════════════")
print(f"✅ Pobrano: {success_count} języków")
print(f"❌ Błędy: {failed_count} języków")
print(f"📁 Lokalizacja: {LOCAL_DIR}")
print(f"═══════════════════════════════════════════════════════════\n")

