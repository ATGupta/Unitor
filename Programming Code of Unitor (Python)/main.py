import imaplib
import email
import datetime

def main():
    global M
    M = imaplib.IMAP4_SSL('imap.gmail.com')

    try:
        M.login('iot.device23@gmail.com', 'internetofthings')
    except imaplib.IMAP4.error:
        print("LOGIN FAILED!!! ")
        # ... exit or deal with failure...

    rv, mailboxes = M.list()
    if rv == 'OK':
        """
        print("Mailboxes:")
        print(mailboxes)
        """

    rv, data = M.select("INBOX")
    if rv == 'OK':
        """
        print("Processing mailbox...\n")
        """
        process_mailbox(M)  # ... do something with emails, see below ...
        M.close()
    M.logout()

def greaterThanLastTime(date_touple):
    global yea, mon, day, hou, minu, sec, mic

    if yea > date_touple.year:
        return False

    if yea == date_touple.year:
        if mon > date_touple.month:
            return False

    if mon == date_touple.month:
        if day > date_touple.day:
            return False

    if day == date_touple.day:
        if hou > date_touple.hour:
            return False

    if hou == date_touple.hour:
        if minu > date_touple.minute:
            return False

    if minu == date_touple.minute:
        if sec > date_touple.second:
            return False

    if sec == date_touple.second:
        if mic > date_touple.microsecond:
            return False

    return True

def equaltoLastTime(local_date):
    global yea, mon, day, hou, minu, sec, mic

    if yea != local_date.year:
        return False

    if mon != local_date.month:
        return False

    if day != local_date.day:
        return False

    if hou != local_date.hour:
        return False

    if minu != local_date.minute:
        return False

    if sec != local_date.second:
        return False

    if mic != local_date.microsecond:
        return False

    return True

def updateMsgLocalDateToLatest(local_date):
    global yea,mon,day,hou,minu,sec,mic

    yea = local_date.year
    mon = local_date.month
    day = local_date.day
    hou = local_date.hour
    minu = local_date.minute
    sec = local_date.second
    mic = local_date.microsecond

def process_mailbox(M):
    rv, data = M.search(None, "ALL")
    if rv != 'OK':
        print("No messages found!")
        return

    for num in data[0].split():
        global last_command

        rv, data = M.fetch(num, '(RFC822)')
        if rv != 'OK':
            print("ERROR getting message", num)
            return

        msg = email.message_from_string(data[0][1])

        date_tuple = email.utils.parsedate_tz(msg['Date'])
        if date_tuple:
            local_date = datetime.datetime.fromtimestamp(
                email.utils.mktime_tz(date_tuple))

            if greaterThanLastTime(local_date) == False:
                continue

            updateMsgLocalDateToLatest(local_date)

            """
            print("Local Date:", \
                local_date.strftime("%a, %d %b %Y %H:%M:%S"))
            """

        if msg['From'] != 'Arya Gupta <arya.tanmay.gupta@gmail.com>':
            continue

        if (msg['Subject'] != 'command') and msg['Subject'] != 'Re: command':
            continue

        body = ''
        if msg.is_multipart():
            body = str(msg.get_payload()[0])
        else:
            body = str(msg.get_payload())

        body = body.splitlines()
        body = body[3]

        if (last_command == body) and (equaltoLastTime(local_date)):
            continue

        last_command = body

        if body == 'on fan1':
            print body
            continue
        if body == 'off fan1':
            print body
            continue
        if body == 'on fan2':
            print body
            continue
        if body == 'off fan2':
            print body
            continue
        """
        M.store(num, '+FLAGS', '\\Deleted')
        M.expunge()
        """
        return

global yea,mon,day,hou,minu,sec,mic,last_command
yea = datetime.date.today().year
mon = datetime.date.today().month
day = datetime.date.today().day
hou = datetime.datetime.now().hour
minu = datetime.datetime.now().minute
sec = datetime.datetime.now().second
mic = datetime.datetime.now().microsecond
last_command=''

while True:
    main()
