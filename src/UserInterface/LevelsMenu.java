/**
 * LevelsMenu - Draws each level of an episode 
 * 				as a clickable button
 * 
 * @author CS319 - Section 2 - Group 9
 */

package UserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import GameAssets.EpisodeType;
import GameManagement.GameEngine;
import GameManagement.MenuManager;

public class LevelsMenu extends Menu
{
	// CONSTANTS
	private final int COLUMN_COUNT = 5;
	// END OF CONSTANTS
	
	// VARIABLES
	private ArrayList<JButton> levelButtons;
    private JButton backButton;
    
	private int levelsCount = 0;
	private int unlockedLevels = 0;          
    private EpisodeType episode;
	// END OF VARIABLES
	
	// CONSTRUCTORS
	public LevelsMenu( EpisodeType episode )
	{
		super();
		
		levelsCount = MenuManager.LEVEL_COUNT;
		unlockedLevels = levelsCount - MenuManager.getInstance().getSettings().
									   getLockedLevelNumber( episode );
		this.episode = episode;
		
		initComponents();
		
		BorderLayout outerLayout = new BorderLayout( 0, 25 );
		
		int rowCount = levelsCount / COLUMN_COUNT;
		if( levelsCount % COLUMN_COUNT != 0 )
			rowCount++;
		
        GridLayout innerLayout = new GridLayout( rowCount, COLUMN_COUNT, 10, 50 );
        JPanel middlePanel = new JPanel();
		
        setLayout( outerLayout );
		middlePanel.setLayout( innerLayout );
		middlePanel.setOpaque( false );
		
		for( JButton b : levelButtons )
		{
			middlePanel.add( b );
		}
		
		// put some white space at the edges so that interface
		// looks nicer
		Dimension gap = new Dimension( 25, 25 );
		add( new Box.Filler( gap, gap, gap ), BorderLayout.PAGE_START );
		add( new Box.Filler( gap, gap, gap ), BorderLayout.LINE_START );
		add( middlePanel, BorderLayout.CENTER );
		add( new Box.Filler( gap, gap, gap ), BorderLayout.LINE_END );
		add( backButton, BorderLayout.PAGE_END );
	}
	// END OF CONSTRUCTORS
	
	// MUTATOR - ACCESSOR METHODS
	public int getUnlockedLevels( String selectedEpisode )
	{
		return unlockedLevels;
	}
	// END OF MUTATOR - ACCESSOR METHODS
	
	// OTHER METHODS
	protected void initComponents()
	{
		levelButtons = new ArrayList<JButton>();
		
		for( int i = 0; i < levelsCount; i++ )
		{
			JButton button = new JButton( "" + ( i + 1 ) );
			
			// Set color and font
	        Font f = new Font( "Default", Font.BOLD, 23 );
	        button.setFont( f );
	        
	        if( i < unlockedLevels )
	        	button.setBackground( new Color( 170 + (int)( Math.random() * 85 ),
	        			170 + (int)( Math.random() * 85 ), 170 + (int)( Math.random() * 85 ) ) );
	        else
	        {
	        	button.setBackground( Color.black );
	        	button.setForeground( Color.white );
	        }
	        
			button.addActionListener( this );
			
			levelButtons.add( button );
		}
        
        backButton = new JButton();
        backButton.setIcon( new ImageIcon( "buttons/BackButton.png" ) );
        backButton.setBorderPainted( false );
        backButton.setContentAreaFilled( false );
        backButton.setFocusPainted( false );
        backButton.addActionListener( this );
    }
	
	public void actionPerformed( ActionEvent e )
	{
        if( e.getSource() == backButton )
        {
        	GameEngine.getInstance().playSound( "sounds/buttonClick.wav" );
    		MenuManager.getInstance().changeMenu( (Menu) new EpisodeMenu() );
        }
        else
        {
        	int i = 0;
        	while( i < levelButtons.size() && e.getSource() != levelButtons.get( i ) )
        	{
        		i++;
        	}
        	
        	if( i != levelButtons.size() )
        	{
        		if( i < unlockedLevels )
        		{
        			// the level user selected is unlocked,
        			// so launch that level and hope that user enjoys
        			// their stay!
        			GameEngine.getInstance().playSound( "sounds/buttonClick.wav" );
        			GameEngine.getInstance().initializeLevel( episode, i + 1 );
        		}
        		else
        		{
        			// the level is locked
        			GameEngine.getInstance().playSound( "sounds/levelLocked.wav" );
        		}
        	}
        }
    } 
	// END OF OTHER METHODS	
}