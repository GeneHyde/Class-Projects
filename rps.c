// This is the client for the Rock Paper Sissors network game I made in my Networks class


#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>


#define MAXLINE 10 /*max text line length*/
#define SERV_PORT 3000 /*port*/
#define USRNAME 26


void strip(char * str)
{
    int x = 0;

    for(x = 0; str[x] != '\0'; x++)
    {
        if(str[x] == '\r' || str[x] == '\n')
            str[x] = '\0';
    }// end while
}// end strip

int main(int argc, char **argv)
{
    fd_set rfd, c_rfd;
    int sockfd, err, choice = 0;
    struct sockaddr_in servaddr;
    char recvline[MAXLINE];
    char temp[USRNAME];

    //basic check of the arguments
    //additional checks can be inserted
    if (argc !=2) {
        perror("Usage: TCPClient <IP address of the server");
        exit(1);
    }

    //Create a socket for the client
    //If sockfd<0 there was an error in the creation of the socket
    if ((sockfd = socket (AF_INET, SOCK_STREAM, 0)) <0) {
        perror("Problem in creating the socket");
        exit(2);
    }

    //Creation of the socket
    memset(&servaddr, 0, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr= inet_addr(argv[1]);
    servaddr.sin_port =  htons(SERV_PORT); //convert to big-endian order

    //printf("Please enter a username:\n");
    //fgets(temp, 26, stdin);

    //Connection of the client to the socket
    if (connect(sockfd, (struct sockaddr *) &servaddr, sizeof(servaddr))<0) {
        perror("Problem in connecting to the server");
        exit(3);
    }
    //send(sockfd, temp, USRNAME, 0);
    while(1) {
        printf("Waiting for room...\n");
        recv(sockfd, temp, MAXLINE, 0);
        puts(temp);
        bzero(temp, MAXLINE);

        printf("Please select an option:\n1. Rock\n2. Paper\n3. Sissors\n4. Exit\n(1,2,3, or 4)\n");
        scanf("%i", &choice);

        if(choice == 1) {
            send(sockfd, "Rock", strlen("Rock"), 0);
        }
        else if(choice == 2) {
            send(sockfd, "Paper", strlen("Paper"), 0);
        }
        else if(choice == 3) {
            send(sockfd, "Scissors", strlen("Scissors"), 0);
        }
        else {
            send(sockfd, "Quit", strlen("Quit"), 0);
            close(sockfd);
            exit(0);
        }
        printf("Waiting for other players choice\n");
        recv(sockfd, recvline, MAXLINE, 0);
        printf("%s\n", recvline);
        if(strcmp(recvline, "Win!") == 0) {
            printf("Finding new player...\n");
        }
        else if(strcmp(recvline, "Lose") == 0) {
            printf("You have been placed back into the que\n");
        }
        else if(strcmp(recvline, "Kill") == 0) {
            printf("You have timed out. Goodbye\n");
            close(sockfd);
            exit(0);
        }
        else {
            printf("Try again\n");
        }
     }

    exit(0);
}
