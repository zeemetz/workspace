#include<stdio.h>
#include<Windows.h>

int queenCount;
char map[8][8];

void holdScan()
{
	getchar();
	getchar();
}

void initMap()
{
	for(int i = 0 ; i < 9 ; i++)
	{
		for(int j = 0 ; j <= 8 ; j++)
		{
			map[i][j]='.';
		}
	}
}

void printMap()
{
	printf("Queen : %d / 8\n",queenCount);
	for(int i = 0 ; i<8 ; i++)
	{
		printf("  %d ",i+1);
	}
	printf("\n");

	for(int i = 0 ; i<8 ; i++)
	{
		printf("====");
	}
	printf("=\n");

	for(int i = 0 ; i < 8 ; i++)
	{
		for(int j = 0 ; j <= 8 ; j++)
		{
			if(j==8)
			{
				printf("| %d ",i+1);
			}
			else
			{
				printf("| %c ",map[i][j]);
			}
		}
		printf("|\n");
	}
	for(int i = 0 ; i<8 ; i++)
	{
		printf("====");
	}
	printf("=\n");
}

bool isWin()
{
	for(int i = 0 ; i < 8 ; i ++)
	{
		for(int j = 0 ; j < 8 ; j ++)
		{
			if(map[i][j]=='.')
			{
				return false;
			}
		}
	}
	/*
	int queenIndex = 0;
	for(int i = 0 ; i < 8 ; i ++)
	{
		for(int j = 0 ; j < 8 ; j ++)
		{
			if(map[i][j]=='Q')
			{
				queenIndex++;
			}
		}
	}*/
	if(queenCount<5)
	{
		system("cls");
		printMap();
		printf("Cant continue the Game, Please use backtrack to finish the game.\nTotal Queen on board must be 8");
		getchar();
		getchar();
		initMap();
		system("cls");
		queenCount=0;
		return false;
	}
	return true;
}

void winMessage()
{
	for(int i = 0 ; i < 5 ; i++)
	{
		printf("\n");
	}
	for(int i = 0 ; i < 30 ; i++)
	{
		printf(" ");
	}
	printf("==============\n");
	for(int i = 0 ; i < 30 ; i++)
	{
		printf(" ");
	}
	printf("    YOU WIN   \n");
	for(int i = 0 ; i < 30 ; i++)
	{
		printf(" ");
	}
	printf("==============\n");
}

void invalidMoveMessage()
{
	for(int i = 0 ; i < 11 ; i++)
	{
		printf("\n");
	}
	for(int i = 0 ; i < 30 ; i++)
	{
		printf(" ");
	}
	printf("==============\n");
	for(int i = 0 ; i < 30 ; i++)
	{
		printf(" ");
	}
	printf("Cant Move Here\n");
	for(int i = 0 ; i < 30 ; i++)
	{
		printf(" ");
	}
	printf("==============\n");
	holdScan();
	system("cls");
}

void invalidInputMessage()
{
	system("cls");
	for(int i = 0 ; i < 11 ; i++)
	{
		printf("\n");
	}
	for(int i = 0 ; i < 30 ; i++)
	{
		printf(" ");
	}
	printf("===============\n");
	
	for(int i = 0 ; i < 30 ; i++)
	{
		printf(" ");
	}
	printf("  Wrong Input  \n");
	for(int i = 0 ; i < 30 ; i++)
	{
		printf(" ");
	}
	printf("===============\n");
	holdScan();
	system("cls");
}

void initQueen(int x , int y)
{
	if(map[y][x]=='*' || map[y][x]=='Q')
	{
		invalidMoveMessage();
		return;
	}
	for(int i = 0 ; i < 8 ; i++)
	{
		for(int j = 0 ; j < 8 ; j++)
		{
			if(map[i][j]!='Q')
			{
				if( j==x || i==y )
					map[i][j]='*';
				if((i+j)==(x+y))
					map[i][j]='*';
				if((i-j)==(y-x))
					map[i][j]='*';
			}
		}
	}
	map[y][x] = 'Q';
	queenCount++;
}

void splashScreen()
{
	printf("N-Queen Problem Game\n");
	printf("BlueJack 13-0\n");
	printf("====================\n");
	printf("N-Queen Problem : \n");
	printf("Put 8 Queen on the map. Map is 8x8. Use backtrack to finish the game\n");
	getchar();
	system("cls");
}

void initGame()
{
	splashScreen();
	initMap();
	queenCount=0;
}

int main()
{
	/*
	splashScreen();
	initMap();
	queenCount=0;
	
	printMap();
	*/
	char replay;
	int x,y;
	do{
		initGame();
		system("cls");
		while(!isWin())
		{
			do{
				x=y=0;
				printMap();
				getchar();
				printf("x-pos [1..8] : ");
				scanf("%d",&x);
				if(x<=0)
				{
					getchar();
					invalidInputMessage();
					continue;
				}
				printf("y-pos [1..8] : ");
				scanf("%d",&y);
				if(y<=0)
				{
					getchar();
					invalidInputMessage();
					continue;
				}
				printf("Put Queen To x : %d and y : %d",x,y);
				holdScan();
			}while( x<1 || x>8 || y < 1 || y > 8 );
			system("cls");
			/*
			if(x==0 && y ==0)
			{
				printf("Give Up");
				getchar();
				getchar();
				return 0;
			}
			else
			{
				initQueen(x-1,y-1);
			}
			*/
			initQueen(x-1,y-1);
		}
		printMap();
		winMessage();
		getchar();
		getchar();
		do{
			system("cls");
			printf("Play Again [Y/N] : ");
			scanf("%c",&replay);
		}while(replay!='N' && replay!='Y');
	}while( replay=='Y' );
	return 0;
}