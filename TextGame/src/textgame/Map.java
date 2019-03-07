/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textgame;

import java.util.Scanner;
import java.util.ArrayList;


/**
 *
 * a class to contain a room[][] array and methods to work a game map
 *
 * @author George Smith
 */
public class Map {

    public static void main(String[] args) {

    }
    //declare and initialize class members
    private Room rooms[][];
    private int playerX;
    private int playerY;
	private ArrayList<Room> roomList = new ArrayList<Room>();

    /**
     * This constructor creates a new map object that represents a map with a
     * specified length and height and sends a default player x and y coordinate
     *
     * @param length the length of the map
     * @param height the height of the map
     *
     * @return a new map with a specified length and height
     */
    public Map(int length, int height) {
        this(length, height, 0, 0);
    }

    /**
     * This constructor creates a new map object that represents a map with a
     * specified length, height, players x and players y position A new room is
     * added to the map by creating a new room object
     *
     * @param length the length of the map
     * @param height the height of the map
     * @param playerx the players x position on the map
     * @param playery the players y position on the map
     *
     * @return a new map with a specified length and height
     */
    public Map(int length, int height, int playerX, int playerY) {
        rooms = new Room[length][height];
		this.playerX = playerX;
		this.playerY = playerY;
    }
	
	public int getPlayerX()
	{
		return playerX;
	}

	public int getPlayerY()
	{
		return playerY;
	}
    /**
     * This method adds a new room to the map, by adding it to the 2d array
     * rooms
     *
     * @param room room object to be added to the array
     * @param x the rooms horizontal position
     * @param y the rooms vertical position on the map
     *
     * @return a 1 or -1 used to show if the room has been added
     */
    public int addRoom(Room room, int x, int y) {
        //check if valid room placement on map
        if (rooms.length - 1 > y && rooms[0].length - 1 > x && x >= 0 && y >= 0) {
            rooms[x][y] = room;
			roomList.add(room);
            return 1;
        }
        return -1;
    }

    /*
     * This method gets the room in the specified position
     *
     * @param x the rooms horizontal position
     * @param y the rooms vertical position on the map
     *
     * @return the room object in that position
     */
    public Room getRoom(int x, int y) {
        //check if the room is on the map
        if (rooms.length - 1 > y && rooms[0].length - 1 > x && x >= 0 && y >= 0) {
            return rooms[x][y];
        }
        return null;
    }
	
	
	public ArrayList<Room> getAllRooms()
	{
		ArrayList<Room> roomListTemp = new ArrayList<Room>();
		
		for(Room rm: roomList)
		{
			roomListTemp.add(rm);
		}
		return roomListTemp;
	}
	public boolean allRoomsCleared()
	{
		for(Room room: roomList)
		{
			if(room.getEnemy() != null)
			{
				return false;
			}
		}
		return true;
		
	}
	

    /**
     * This method completes the action chosen by the player for a movement on
     * the map
     *
     * @param input The user's command to the player
     *
     * @return a value of i relating to the direction the player moved for
     * testing purposes
     */
    public int runAction(String input) {
        //get the action the player chose
        int i = rooms[playerX][playerY].doAction(input);

        //if doAction returns one of these values the player chose to move
        //else they chose a different command 
        Player pMain = this.getRoom(playerX, playerY).getPlayer(); 
	
		if(pMain.getHealth() > 0)
		{
			if (i == 10) {
				Player p = this.getRoom(playerX, playerY).removePlayer();
				playerY--;
				p.setY(playerY);
				this.getRoom(playerX, playerY).addPlayer(p);
			} else if (i == 11) {
				Player p = this.getRoom(playerX, playerY).removePlayer();
				playerX++;
				p.setX(playerX);
				this.getRoom(playerX, playerY).addPlayer(p);
			} else if (i == 12) {
				Player p = this.getRoom(playerX, playerY).removePlayer();
				playerY++;
				p.setY(playerY);
				this.getRoom(playerX, playerY).addPlayer(p);
			} else if (i == 13) {
				Player p = this.getRoom(playerX, playerY).removePlayer();
				playerX--;
				p.setX(playerX);
				this.getRoom(playerX, playerY).addPlayer(p);
			} else if (i == 14) {
				Player p = this.getRoom(playerX, playerY).getPlayer();
				Enemy e = this.getRoom(playerX, playerY).getEnemy();
				p.attackEnemy(e);
				//p.takeDamage(1);
				if(e.getHealth() > 0)
				{
					e.attack(p);
				}
				
				System.out.println(e.toString() + "\n");
				System.out.print(p.toString() + "\n");
				
				
				if(e.getHealth() <= 0)
				{
					e = this.getRoom(playerX, playerY).removeEnemy();
					System.out.print("Enemy dead \n");

				}
			if(pMain.getHealth() <= 0)
			{
				System.out.print("Game Over. You have been slain. \n");
				return 0;
			}
		}
			else if (i == 15) {
				Player p = this.getRoom(playerX, playerY).getPlayer();
				ArrayList<String> inventory = p.getPouch();
				int numItems = 1;
				for(String item: inventory)
				{
					System.out.println(numItems + ". " + item);
					numItems++;
				}
				Scanner itemScanner = new Scanner(System.in);
				String itemSelection = itemScanner.nextLine();
				
				int item = p.useItem(itemSelection);
			}
				

        }
		

        return i;
    }

    /**
     * This method prints the map to the console
     *
     * @return the string variable corresponding to the map
     */
    public String printMap() {
        String s = "";
        for (int row = 0; row < (rooms.length * 2) + 1; row++) {
            s = s + this.printRow(row) + "\n";
        }
        return s;
    }

    /**
     * This method prints the row for the map
     *
     * @param rowNumber current row to be printed
     *
     * @return the string variable corresponding to row on the map
     */
    private String printRow(int rowNumber) {
        String s = "";
        if (rowNumber % 2 == 1) {
            s = s + this.printCellInter(rooms[0].length, rowNumber / 2);
        } else {
            s = s + this.printLine(rooms[0].length, rowNumber / 2);
        }

        return s;
    }

    /**
     * This method prints the line to distinguish the rooms on the map
     *
     * @param numberOfNodes number of rooms
     *
     * @return the string variable corresponding to a line on the map
     */
    private String printLine(int numberOfNodes, int row) {
        String s = "";
        for (int i = 0; i < (numberOfNodes); i++) {
            if (row < this.rooms[0].length && this.rooms[(i)][row] != null
                    || (row > 0 && this.rooms[(i)][row - 1] != null)) {
                s = s + "--";
            } else {
                s = s + "  ";
            }

            /*
            if (row < this.rooms.length && this.rooms[(row)][i] == null) {
                s = s + "  ";
            } else if (row>0 && this.rooms[(row-1)][i] == null) {
                s = s + "--";
            } else if (!(row < this.rooms.length)) {
                s = s + "  ";
            } else {
                s = s + "--";
            }
             */
        }

        return s;
    }

    /**
     * This method prints the contents in the middle of the cell
     *
     * @param numberOfNodes number of rooms
     *
     * @return the string variable corresponding to the middle of a cell on the
     * map
     */
    private String printCellInter(int numberOfNodes, int row) {
        String s = "";
        boolean wasLastRoom = false;
        for (int i = 0; i < numberOfNodes; i++) {
            if (this.rooms[i][row] == null) {

                if (wasLastRoom) {
                    s = s + "|";
                    wasLastRoom = false;
                } else {
                    s = s + " ";
                }
                s = s + " ";
            } else {
                s = s + "|";
                wasLastRoom = true;
                if (this.playerX == i && this.playerY == row) {
                    s = s + "p";
                } else {
                    s = s + " ";
                }
            }
        }
        //s = s + "|";
        return s;

    }

    /**
     * Our own toString method for this class to print a room on the map
     *
     * @param row row of room to print
     * @param column column of room to print
     *
     * @return the information about the room to the user
     */
    public String toString(int row, int column) {
        if (rooms[0].length - 1 > row && rooms.length - 1 > column && column >= 0 && row >= 0 && rooms[column][row] != null) {
            return rooms[column][row].toString();
        } else {
            return "no room exists there";
        }
    }

}
