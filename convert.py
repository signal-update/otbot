import os

os.system("ls *.docx")

# input dari user
target = input("Hapus file tanggal?: ")

# hapus file/folder yang ada 'target' di namanya (lokal + git)
os.system(f"rm -rf *{target}* && git rm -rf *{target}*")

# commit dan push hapusannya
os.system('git commit -m "hapus file lama"')
os.system("git push")

# upload file baru
os.system("git add .")
os.system('git commit -m "upload file baru"')
os.system("git push origin main")
