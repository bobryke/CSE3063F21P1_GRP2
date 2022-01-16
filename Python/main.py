"""
   ___ ___ ___ ____ __   __ ____
  / __/ __| __|__ //  \ / /|__ /
 | (__\__ \ _| |_ \ () / _ \|_ \
  \___|___/___|___/\__/\___/___/__
 | _ \ _ \/ _ \ _ | | __/ __|_   _|
 |  _/   / (_) | || | _| (__  | |
 |_| |_|_\\___/ \__/|___\___| |_|
 CSE3063 PROJECT
 ONLINE STUDENT REGISTRATION SYSTEM
 ITERATION-1 CODE
 */
//  ********************************
//  * Authors:                     *
//  * ONURCAN ISLER 150120825      *
//  * BERKAY MENGUNOGUL 150119934  *
//  * SINAN DUMANSIZ 150119812     *
//  * MERVE HAZAL OZALP 150120827  *
//  * KEREM KOSIF 150119909        *
//  * MURAT TUZUN 150119633        *
//  ********************************

"""
from system.registrationSystem import RegistrationSystem

import logging
logging.basicConfig(filename='log.txt', encoding='utf-8', level=logging.DEBUG)

def main():
     logging.info("Simulation started")     
     registration_system = RegistrationSystem()
     registration_system.batch_process()
     logging.info("Simulation ended")
if __name__ == "__main__":
  main()  