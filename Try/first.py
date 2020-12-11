import re

with open('input.txt') as fp:
    Lines = fp.readline()
    countValid = 0
    countFields = 0
    falsePass = True
    while Lines:
        while ((not Lines == "\n") and falsePass == False):
            Lines = fp.readline()
        if not Lines == "\n":
            falsePass = True
            split = Lines.split(" ")
            for s in split:
                ssplit = s.split(":")
                if not (ssplit[0] == "cid"):
                    #hasCid = True
                    countFields = countFields + 1
                if (ssplit[0] == "byr"):
                    if not (int(ssplit[1]) >= 1920 and int(ssplit[1]) <= 2002):
                        falsePass = False
                        break
                if (ssplit[0] == "iyr"):
                    if not (int(ssplit[1]) >= 2010 and int(ssplit[1]) <= 2020):
                        falsePass = False
                        break
                if (ssplit[0] == "eyr"):
                    if not (int(ssplit[1]) >= 1920 or int(ssplit[1]) <= 2002):
                        falsePass = False
                        break
                if (ssplit[0] == "byr"):
                    if not (ssplit[1] >= 1920 or ssplit[1] <= 2002):
                        falsePass = False
                        break
                if (ssplit[0] == "byr"):
                    if not (ssplit[1] >= 1920 or ssplit[1] <= 2002):
                        falsePass = False
                        break
                if (ssplit[0] == "byr"):
                    if not (ssplit[1] >= 1920 or ssplit[1] <= 2002):
                        falsePass = False
                        break                        
                     

                        
        else: 
            #if (countFields == 7 and hasCid == False) or (countFields == 8):
            if countFields == 7:
                countValid = countValid + 1
            #hasCid = False
            countFields = 0
        Lines = fp.readline()
    print(countValid)
    