# update_structure.py
import os

def generate_tree(start_path='.', prefix=''):
    ignore_dirs = {'.git', 'build', '.gradle', 'node_modules', '.idea', '.vscode', '__pycache__'}
    tree = ""
    entries = sorted([e for e in os.listdir(start_path) if not e.startswith('.') or e == '.github'])
    for idx, entry in enumerate(entries):
        path = os.path.join(start_path, entry)
        if entry in ignore_dirs:
            continue
        connector = "└── " if idx == len(entries) - 1 else "├── "
        tree += f"{prefix}{connector}{entry}\n"
        if os.path.isdir(path):
            extension = "    " if idx == len(entries) - 1 else "│   "
            tree += generate_tree(path, prefix + extension)
    return tree

def update_readme():
    with open("README.md", "r", encoding="utf-8") as f:
        content = f.read()

    tree = generate_tree(".")
    section = "## 📂 Project Structure\n\n```\n" + tree.strip() + "\n```"

    start = content.find("## 📂 Project Structure")
    end = content.find("##", start + 1)
    if end == -1:
        updated = content[:start] + section
    else:
        updated = content[:start] + section + "\n\n" + content[end:]

    with open("README.md", "w", encoding="utf-8") as f:
        f.write(updated)

if __name__ == "__main__":
    update_readme()
# This script generates a tree structure of the project directory and updates the README.md file with it.
# It ignores certain directories like .git, build, .gradle, node_modules, .idea, .vscode, and __pycache__.
# The tree structure is formatted with connectors to indicate the hierarchy of files and directories.
# The script reads the README.md file, finds the section for the project structure, and replaces it with the new tree structure.
# If the section does not exist, it appends the new section at the end of the file.
# The updated README.md file is then written back to the disk.