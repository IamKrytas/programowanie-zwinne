# update_languages.py
import requests
import os

REPO_OWNER = "IamKrytas"
REPO_NAME = "programowanie-zwinne"
API_URL = f"https://api.github.com/repos/{REPO_OWNER}/{REPO_NAME}/languages"

response = requests.get(API_URL)
languages = response.json()

total = sum(languages.values())
lines = ["## 📊 Languages Used\n", "```"]

for lang, count in sorted(languages.items(), key=lambda x: x[1], reverse=True):
    percent = (count / total) * 100
    lines.append(f"{lang:<12} {percent:.1f}%")

lines.append("```")

# Replace old section in README.md
with open("README.md", "r", encoding="utf-8") as f:
    content = f.read()

start = content.find("## 📊 Languages Used")
end = content.find("##", start + 1)

new_section = "\n".join(lines)
if end != -1:
    updated = content[:start] + new_section + "\n\n" + content[end:]
else:
    updated = content[:start] + new_section

with open("README.md", "w", encoding="utf-8") as f:
    f.write(updated)
# This script fetches the languages used in a GitHub repository and updates the README.md file with the information.
# It uses the GitHub API to get the languages and their respective lines of code.
# The script calculates the percentage of each language used and formats it into a markdown section.
# The section is then replaced in the README.md file, or added if it doesn't exist.
# The updated README.md file is written back to the disk.
