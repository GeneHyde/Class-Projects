#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/time.h>
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/queue.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <errno.h>

#define MAXLINE 10 /*max text line length*/
#define SERV_PORT 3000 /*port*/
#define LISTENQ 10 /*maximum number of client connections */
#define ROOMNUMS 2

STAILQ_HEAD(stailhead, entry) head;
struct stailhead *headp;
fd_set rfd, c_rfd;
struct timeval tv;
char temp [MAXLINE];


struct entry {
    int fd;
    STAILQ_ENTRY(entry) entries;
};
struct room {
    int p1;
    int p2;
    int move1;
    int move2;
    int clock;
    int playeramount;
}; typedef struct room Room;

void add_to_queue(int ai) {
    //char temp [USRNAME];
    struct entry *elem;
    elem = calloc(1, sizeof(struct entry));
    //int err = recv(ai, temp, USRNAME, 0);
    if (elem) { // && err > 0) {
        //strcpy(elem->name, temp);
        elem->fd = ai;
    }
    STAILQ_INSERT_TAIL(&head, elem, entries);
}

int remove_from_queue() {
    if(!STAILQ_EMPTY(&head)) {
        struct entry *elem = STAILQ_FIRST(&head);
        int ret = elem->fd;
        STAILQ_REMOVE_HEAD(&head, entries);
        free(elem);
        return ret;
    }
    return -1;
}

void check(int * move, char * recv) {
    printf("inside check\n");
    if(strcmp(recv, "Rock") == 0) {
        *move = 0;
    }
    else if(strcmp(recv, "Paper") == 0) {
        *move = 1;
    }
    else if(strcmp(recv, "Scissors") == 0) {
        *move = 2;
    }
    else {
        *move = 5;
    }
}

void player1Win(Room * r) {
    send(r->p1, "Win!", strlen("Win!"), 0);
    send(r->p2, "Lose", strlen("Lose"), 0);
    add_to_queue(r->p2);
    r->p2 = -1;
    r->playeramount--;
    r->clock = 0;
}

void player2Win(Room * r) {
    send(r->p2, "Win!", strlen("Win!"), 0);
    send(r->p1, "Lose", strlen("Lose"), 0);
    add_to_queue(r->p1);
    r->p1 = -1;
    r->playeramount--;
    r->clock = 0;
}

void play(Room * r) {

    if(r->p1 != -1 && r->p2 != -1) {
        if(r->clock == 0) {
            r->move1 = -1; r->move2 = -1;
            /*
            FD_ZERO(&rfd); FD_SET(r->p1, &rfd);
            printf("above p1 clear\n");
            select(r->p1 + 1, &rfd, NULL, NULL, &tv);
            while(FD_ISSET(r->p1, &rfd)) {
                read(r->p1, &temp, MAXLINE);
            }

            FD_ZERO(&rfd); FD_SET(r->p2, &rfd);
            printf("above p2 clear\n");
            select(r->p2 + 1, &rfd, NULL, NULL, &tv);
            while(FD_ISSET(r->p2, &rfd)) {
                read(r->p2, &temp, MAXLINE);
            }
            //*/
            printf("sending go to p1\n");
            if(send(r->p1, "Go!\n", strlen("Go!\n"), 0) <= 0){
                r->p1 = -1;
                printf("p1 did not work\n");
            }
            printf("sending go to p2\n");
            if(send(r->p2, "Go!\n", strlen("Go!\n"), 0) <= 0){
                r->p2 = -1;
                printf("p2 did not work\n");
            }

            if(r->p1 == -1 && r->p2 == -1) {
                r->playeramount-= 2;
                return;
            }
            else if(r->p1 == -1) {
                send(r->p2, "Win!", strlen("Win!"), 0);
                r->playeramount--;
                return;
            }
            else if(r->p2 == -1) {
                send(r->p1, "Win!", strlen("Win!"), 0);
                r->playeramount--;
                return;
            }
            r->clock++;
        }
        else if((r->move1 == -1 || r->move2 == -1)) {

                FD_ZERO(&rfd); FD_SET(r->p1, &rfd);
                select(r->p1 + 1, &rfd, NULL, NULL, &tv);
                if(FD_ISSET(r->p1, &rfd) && r->move1 == -1) {
                    printf("inside 1\n");
                    recv(r->p1, &temp, MAXLINE, 0);
                    printf("got %s\n", temp);
                    check(&r->move1, temp);
                    if(r->move1 != -1) {
                        printf("p1 move is %i\n", r->move1);
                    }
                    bzero(temp, MAXLINE);
                }

                FD_ZERO(&rfd); FD_SET(r->p2, &rfd);
                select(r->p2 + 1, &rfd, NULL, NULL, &tv);
                if(FD_ISSET(r->p2, &rfd) && r->move2 == -1) {
                    printf("inside 2\n");
                    recv(r->p2, &temp, MAXLINE, 0);
                    printf("got %s\n", temp);
                    check(&r->move2, temp);
                    if(r->move2 != -1) {
                        printf("p2 move is %i\n", r->move2);
                    }
                    bzero(temp, MAXLINE);
                }
                //sleep(1);
                r->clock += 1;
        }
        else {
            printf("finish\n");
            if(r->move1 == 5 || r->move2 == 5) {
                if(r->move1 == 5) {
                    close(r->p1);
                    r->p1 = -1;
                }
                if(r->move2 == 5) {
                    close(r->p2);
                    r->p2 = -1;
                }
            }
            if(r->move1 == -1) {
                send(r->p1, "Kill", strlen("Kill"), 0);
                close(r->p1);
                r->p1 = -1;
            }
            if(r->move2 == -1) {
                send(r->p2, "Kill", strlen("Kill"), 0);
                close(r->p2);
                r->p2 = -1;
            }
            if(r->p1 == -1 && r->p2 == -1) {
                r->clock = 0;
                r->playeramount-= 2;
            }
            else if(r->p1 == -1) {
                send(r->p2, "Win!", strlen("Win!"), 0);
                r->clock = 0;
                r->playeramount--;
            }
            else if(r->p2 == -1) {
                send(r->p1, "Win!", strlen("Win!"), 0);
                r->clock = 0;
                r->playeramount--;
            }
            else if(r->move2 == 2 && r->move1 == 0) {
                player1Win(r);
            }
            else if(r->move2 == 0 && r->move1 == 2) {
                player2Win(r);
            }
            else if(r->move2 > r->move1) {
                player2Win(r);
            }
            else if(r->move2 < r->move1) {
                player1Win(r);
            }
            else {
                //tie
                send(r->p1, "Tie.", strlen("Tie."), 0);
                send(r->p2, "Tie.", strlen("Tie."), 0);
                r->clock = 0;
            }
            sleep(1);
        }
    }
    else if(!STAILQ_EMPTY(&head)) {
        if(r->p1 == -1) {
            r->p1 = remove_from_queue();
            printf("p1 connected to %i\n", r->p1);
            r->playeramount++;
        }
        else if(r->p2 == -1) {
            r->p2 = remove_from_queue();
            printf("p2 connected to %i\n", r->p2);
            r->playeramount++;
        }
    }
}

int main()
{
    fd_set rset, allset;
    int rv, ch;
    Room *room1, *room2;
    int listenfd, connfd, errno;
    socklen_t clilen;
    struct sockaddr_in cliaddr, servaddr;
    int i;
    struct timeval atv;
    //Room ** rooms;



    //queue initilization
    STAILQ_INIT(&head);

    //creation of the socket
    listenfd = socket (AF_INET, SOCK_STREAM, 0);

    //preparation of the socket address
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    servaddr.sin_port = htons(SERV_PORT);

    bind(listenfd, (struct sockaddr *) &servaddr, sizeof(servaddr));

    listen(listenfd, LISTENQ);
    printf("%s\n","Server running...waiting for connections.");

    tv.tv_sec = 1;
    tv.tv_usec = 0;

    FD_ZERO(&allset);
	FD_SET(listenfd, &allset);
    fcntl (listenfd, F_SETFL, O_NONBLOCK);
    //rooms = (Room **)calloc(ROOMNUMS, sizeof(Room *); /*
    
    
    room1 = (Room *)calloc(1,sizeof(Room));
    room1->p1 = -1;
    room1->p2 = -1;
    room1->move1 = -1;
    room1->move2 = -1;

    room2 = (Room *)calloc(1,sizeof(Room));
    room2->p1 = -1;
    room2->p2 = -1;
    room2->move1 = -1;
    room2->move2 = -1;
    
    //*/for(i = 0; i < ROOMNUMS; i++) {
    /*      rooms[i] = (Room *)calloc(1,sizeof(Room));
            rooms[i]->p1 = -1;
            rooms[i]->p2 = -1;
            rooms[i]->move1 = -1;
            rooms[i]->move2 = -1;
    }*/


    for ( ; ; ) {

    rset = allset;
    select(listenfd + 1, &rset, NULL, NULL, &tv);
    if (FD_ISSET(listenfd, &rset)) {	/* new client connection */
        //sleep(1);
        clilen = sizeof(cliaddr);
        connfd = accept(listenfd, (struct sockaddr *) &cliaddr, &clilen);
        if(connfd != -1) {
            printf("%s\n","Received request...");
            printf("added to que\n");
            add_to_queue(connfd);
            listen(listenfd, LISTENQ);
        }
    }
        play(room1);
        play(room2);
        if(room1->playeramount == 1 && room2->playeramount == 1) {
            if(room1->p1 == -1) {
                if(room2->p1 != -1) {
                    room1->p1 = room2->p1;
                    room2->p1 = -1;
                    room2->playeramount--;
                    room1->playeramount++;
                }
                else if(room2->p2 != -1) {
                    room1->p1 = room2->p2;
                    room2->p2 = -1;
                    room2->playeramount--;
                    room1->playeramount++;
                }
            }
            else if(room1->p2 == -1) {
                if(room2->p1 != -1) {
                    room1->p2 = room2->p1;
                    room2->p1 = -1;
                    room2->playeramount--;
                    room1->playeramount++;
                }
                else if(room2->p2 != -1) {
                    room1->p2 = room2->p2;
                    room2->p2 = -1;
                    room2->playeramount--;
                    room1->playeramount++;
                }
            }
        }

    }
    //close listening socket
    close(listenfd);
    free(room1);
    free(room2);
    /*
    for(i = 0; i < ROOMNUMS; i++) {
        free(rooms[i]);
    }
    free(rooms);
    */
}
