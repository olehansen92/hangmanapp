package com.example.hangman;

import java.util.Arrays;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends Activity {
    
	private MediaPlayer mediaPlayer;
	private StringBuilder uferdigOrd;
	private String ferdigOrd, randomOrd, riktigBokstavString, feilBokstavString;
	private String[] ordTabell;
	private int feilGjettTeller, riktigGjettTeller, ordTeller;
	public static int poeng, hiscore, nyHiscore, riktigeOrd, globOrdTeller, ordRiktigTeller, ordFeilTeller, spillTeller, spillVunnetTeller, spillTaptTeller;
	private TextView ordFelt, multiFelt, poengFelt, ordOversiktFelt;
	private Button A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Rb, S, T, U, V, W, X, Y, Z;
	private ImageView imageHangman;
	private AlertDialog.Builder dialogBuilder;
	boolean forrigeRett, fForrigeRett;

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		mediaPlayer = MediaPlayer.create(this, R.raw.riktig1);
		imageHangman = (ImageView)findViewById(R.id.imageHangman);
		spillTeller++;
		
		deklarerKnapper();
		
		ordFelt = (TextView) findViewById(R.id.textRulesView);
		ordOversiktFelt = (TextView) findViewById(R.id.ordOversiktTekst);
		ordOversiktFelt.setText(ordTeller + "/5");
		multiFelt = (TextView) findViewById(R.id.textMulti);
		poengFelt = (TextView) findViewById(R.id.textPoeng);
		
		ordTabell = new String[5];
		Arrays.fill(ordTabell, null);

		ordTeller = 0;
		poeng = 0;
		nyttSpill();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	
	
	
	@Override
	public void onBackPressed() 
	{
		avsluttSpillDialog();
	}

	private void avsluttSpillDialog()
	{
		dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setCancelable(false);
		dialogBuilder.setTitle(getString(R.string.dialogAvsluttTittel));
		dialogBuilder.setMessage(getString(R.string.dialogAvsluttSpill));
		
		dialogBuilder.setPositiveButton((getString(R.string.yes)), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
				spillTaptTeller++;
				ordFeilTeller++;
				finish();
			}
		});
		
		dialogBuilder.setNegativeButton((getString(R.string.no)), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				
				dialog.dismiss();
			}
		});
		
		AlertDialog alert = dialogBuilder.create();
		alert.show();
		
	}
	
	
	
	
	public void nyttSpill()
	{
		riktigBokstavString=""; //for orientation change
		feilBokstavString="";   //for orientation change
		forrigeRett = false;
		fForrigeRett = false;
		feilGjettTeller = 0;
		riktigGjettTeller = 0;
		ferdigOrd = trekkOrd();
		
		uferdigOrd = new StringBuilder();	
		uferdigOrd.append(ferdigOrd);
		
		for(int i = 0; i < uferdigOrd.length(); i++)
		{
			uferdigOrd.setCharAt(i, '_');
			i++;
		}
		ordFelt.setText(uferdigOrd);
		ordOversiktFelt.setText(ordTeller + "/5");
		
	}
	
	public int getHiscore()
	{
		return hiscore;
	}
	
	public void gjett(char a, Button s)
	{
		boolean riktigGjett = false;
		s.setEnabled(false);
		
		for(int i = 0; i < uferdigOrd.length(); i++)
		{
			if (ferdigOrd.charAt(i) == a)
			{
				riktigBokstavString += a;
				regnUtMultiplier();
				riktigGjett = true;
				riktigGjettTeller++;
				uferdigOrd.setCharAt(i, a);
				ordFelt.setText(uferdigOrd);
				poengFelt.setText(String.valueOf(poeng));
				s.setBackgroundResource(R.xml.rounded_green);
				forrigeRett = true;
				spillRiktigLyd();
				
				
				
				if(ferdigOrd.toString().equals(uferdigOrd.toString()))
				{
					resetLyd();
	            	ordRiktigTeller++;
					if(poeng > hiscore)
					{
						hiscore = poeng;
						spillFerdig();
					}
					else
					{
						spillFerdig();
					}
					
				}
			}
		}
		
		if(riktigGjett == false)
		{
			feilBokstavString += a;
			forrigeRett=false;
			fForrigeRett=false;
			feilGjettTeller++;

			s.setBackgroundResource(R.xml.rounded_red);
			visMultiplier(" ");
			
			
			resetLyd();              	
        	mediaPlayer = MediaPlayer.create(this, R.raw.feil);
        	mediaPlayer.start();
		
        	tegnHangman();
		}
	}
	
	private void regnUtMultiplier()
	{
		int hjelp = (1000 / (uferdigOrd.length() / 2));
		
		if(forrigeRett == true)
		{
			if(fForrigeRett==true)
			{
				poeng += (hjelp * 4);
				visMultiplier("4x");
			}
			else
			{
				poeng += (hjelp * 2);
				fForrigeRett=true;
				visMultiplier("2x");
			}
		}
		else
		{
			poeng += hjelp;
		}
	}
	
	private void spillRiktigLyd()
	{
		if(riktigGjettTeller >= 1)
		{
			switch(riktigGjettTeller){
            case 1:	
            	resetLyd();  
            	mediaPlayer = MediaPlayer.create(this, R.raw.riktig1);
            	mediaPlayer.start();
            break;
            case 2:	
            	resetLyd();  	            	
            	mediaPlayer = MediaPlayer.create(this, R.raw.riktig2);
            	mediaPlayer.start();
            break;
            case 3:	
            	resetLyd();  	            	
            	mediaPlayer = MediaPlayer.create(this, R.raw.riktig3);
            	mediaPlayer.start();	            	
            break;
            case 4:	
            	resetLyd();  	            	
            	mediaPlayer = MediaPlayer.create(this, R.raw.riktig4);
            	mediaPlayer.start();
            break;
            case 5:	
            	resetLyd();  	            	
            	mediaPlayer = MediaPlayer.create(this, R.raw.riktig5);
            	mediaPlayer.start();
            break;
            case 6:
            	resetLyd();  	            	
            	mediaPlayer = MediaPlayer.create(this, R.raw.riktig6);
            	mediaPlayer.start();
            break;
			}
		}
		else
		{
        	resetLyd();            	
        	mediaPlayer = MediaPlayer.create(this, R.raw.riktig6);
        	mediaPlayer.start();
		}
	}
	
	private void resetLyd()
	{
    	mediaPlayer.stop();
    	mediaPlayer.reset();
    	mediaPlayer.release();
    	mediaPlayer = null;		
	}
	
	private void tegnHangman()
	{
		if(feilGjettTeller >= 1)
		{
			switch(feilGjettTeller)
	    	{
	    	case 1:
	    		imageHangman.setImageResource(R.drawable.h1_0);
	    		imageHangman.setVisibility(View.VISIBLE);
	        break;
	    	case 2:
	    		imageHangman.setImageResource(R.drawable.h1_1);
	        break;
	    	case 3:
	    		imageHangman.setImageResource(R.drawable.h1);
	        break;
	    	case 4:
	    		imageHangman.setImageResource(R.drawable.h2);
	        break;
	    	case 5:
	    		imageHangman.setImageResource(R.drawable.h3);
	        break;
	    	case 6:
	    		imageHangman.setImageResource(R.drawable.h4);
	        break;
	    	case 7:
	    		imageHangman.setImageResource(R.drawable.h5);
	        break;
	    	case 8:
	    		imageHangman.setImageResource(R.drawable.h6);
	    		mediaPlayer.stop();
	        	mediaPlayer.reset();
	        	mediaPlayer.release();
	        	mediaPlayer = null;		            	
	        	mediaPlayer = MediaPlayer.create(this, R.raw.tapelyd);
	        	mediaPlayer.start();
	        	ordFeilTeller++;
	        	spillTaptTeller++;
	        	feilOrdDialog();
	        break;
	        
	    	}
		}
		
	}
	
	public void bestemOrd()
	{
		String[] ord = getResources().getStringArray(R.array.ordArray);
		randomOrd = ord[new Random().nextInt(ord.length)];
		
		for(int i=0; i < ordTabell.length; i++)
		{
			if(Arrays.asList(ordTabell).contains(randomOrd))
			{
				//Checks whether a word is already been used or not.
				break;
			}
			else if(ordTabell[i] == null)
			{
				ordTabell[i] = randomOrd;
				ordTeller++;
				globOrdTeller++;
				return;
			}
		}
		bestemOrd();
	}
	
	/* 
	 * Resets the buttons appearance after a word is correctly guessed - ready for a new round. 
	 */
	
	public String trekkOrd()
	{
		bestemOrd();
		return randomOrd;
	}
	
	
	/* 
	 * Finishes the round or initializes a new word (if under five words are guessed so far). 
	 */
	
	public void spillFerdig()
	{   
    	mediaPlayer = MediaPlayer.create(this, R.raw.vinn);
    	mediaPlayer.start();
    	riktigeOrd++;
    	deaktiverKnapper();
    	
		Handler handler = new Handler(); 
		handler.postDelayed(new Runnable() { 
		   public void run() { 
		    	if(ordTeller == 5)
		    	{
		    		spillVunnetTeller++;
		    		finish();
		    	}
		    	else
		    	{
		    		imageHangman.setImageResource(R.drawable.h0);
		    		tilbakestillKnapper();
		    		nyttSpill();
		    	}
		   } 
	    }, 3000); 
    	

		
	}
	
	/* 
	 * Resets the buttons appearance after a word is correctly guessed - ready for a new round. 
	 */
	private void tilbakestillKnapper()
	{
		A.setBackgroundResource(R.xml.rounded_blue); A.setEnabled(true);
		B.setBackgroundResource(R.xml.rounded_blue); B.setEnabled(true);
		C.setBackgroundResource(R.xml.rounded_blue); C.setEnabled(true);
		D.setBackgroundResource(R.xml.rounded_blue); D.setEnabled(true);
		E.setBackgroundResource(R.xml.rounded_blue); E.setEnabled(true);
		F.setBackgroundResource(R.xml.rounded_blue); F.setEnabled(true);
		G.setBackgroundResource(R.xml.rounded_blue); G.setEnabled(true);
		H.setBackgroundResource(R.xml.rounded_blue); H.setEnabled(true);
		I.setBackgroundResource(R.xml.rounded_blue); I.setEnabled(true);
		J.setBackgroundResource(R.xml.rounded_blue); J.setEnabled(true);
		K.setBackgroundResource(R.xml.rounded_blue); K.setEnabled(true);
		L.setBackgroundResource(R.xml.rounded_blue); L.setEnabled(true);
		M.setBackgroundResource(R.xml.rounded_blue); M.setEnabled(true);
		N.setBackgroundResource(R.xml.rounded_blue); N.setEnabled(true);
		O.setBackgroundResource(R.xml.rounded_blue); O.setEnabled(true);
		P.setBackgroundResource(R.xml.rounded_blue); P.setEnabled(true);
		Q.setBackgroundResource(R.xml.rounded_blue); Q.setEnabled(true);
		Rb.setBackgroundResource(R.xml.rounded_blue); Rb.setEnabled(true);
		S.setBackgroundResource(R.xml.rounded_blue); S.setEnabled(true);
		T.setBackgroundResource(R.xml.rounded_blue); T.setEnabled(true);
		U.setBackgroundResource(R.xml.rounded_blue); U.setEnabled(true);
		V.setBackgroundResource(R.xml.rounded_blue); V.setEnabled(true);
		W.setBackgroundResource(R.xml.rounded_blue); W.setEnabled(true);
		X.setBackgroundResource(R.xml.rounded_blue); X.setEnabled(true);
		Y.setBackgroundResource(R.xml.rounded_blue); Y.setEnabled(true);
		Z.setBackgroundResource(R.xml.rounded_blue); Z.setEnabled(true);	
	}
	
	private void tegnRiktigeKnapper(Button s)
	{
		s.setBackgroundResource(R.xml.rounded_green); s.setEnabled(false);
	}
	
	private void tegnFeilKnapper(Button s)
	{
		s.setBackgroundResource(R.xml.rounded_red); s.setEnabled(false);
	}
	
	
	/* 
	 * Makes sure no buttons are clickable at certain stages of the game.
	 */
	private void deaktiverKnapper()
	{
		A.setEnabled(false);
		B.setEnabled(false);
		C.setEnabled(false);
		D.setEnabled(false);
		E.setEnabled(false);
		F.setEnabled(false);
		G.setEnabled(false);
		H.setEnabled(false);
		I.setEnabled(false);
		J.setEnabled(false);
		K.setEnabled(false);
		L.setEnabled(false);
		M.setEnabled(false);
		N.setEnabled(false);
		O.setEnabled(false);
		P.setEnabled(false);
		Q.setEnabled(false);
		Rb.setEnabled(false);
		S.setEnabled(false);
		T.setEnabled(false);
		U.setEnabled(false);
		V.setEnabled(false);
		W.setEnabled(false);
		X.setEnabled(false);
		Y.setEnabled(false);
		Z.setEnabled(false);	
	}
	
	
	/* 
	 * Shows a point multiplier on the screen when the player guesses multiple correct letters in a row.
	 */
	private void visMultiplier(String multi)
	{
		if(forrigeRett == true)
		{
			multiFelt.setText(multi);
			multiFelt.setVisibility(View.VISIBLE);
		}
		else
		{
			multiFelt.setVisibility(View.GONE);
		}
	}
	
	
	/* 
	 * Button listener for the character-buttons.
	 */
	private OnClickListener onClickListener = new OnClickListener() {
		 @Override
	     public void onClick(View v) 
	     {
	         switch(v.getId()){
	             case R.id.bA:
	                  gjett('A', A);
	          
	             break;
	             case R.id.bB:
	            	 gjett('B', B);
	            
	             break;
	             case R.id.bC:
	            	 gjett('C', C);
	            
	             break;
	             case R.id.bD:
	                  gjett('D', D);
	        
	             break;
	             case R.id.bE:
	            	 gjett('E', E);
	            	
	             break;
	             case R.id.bF:
	            	 gjett('F', F);
	          
	             break;
	             case R.id.bG:
	                  gjett('G', G);
	           
	             break;
	             case R.id.bH:
	            	 gjett('H', H);
	        
	             break;
	             case R.id.bI:
	            	 gjett('I', I);
	            	
	             break;
	             case R.id.bJ:
	                  gjett('J', J);
	             break;
	             case R.id.bK:
	            	 gjett('K', K);
	             break;
	             case R.id.bL:
	            	 gjett('L', L);
	             break;
	             case R.id.bM:
	                  gjett('M', M);
	             break;
	             case R.id.bN:
	            	 gjett('N', N);
	             break;
	             case R.id.bO:
	            	 gjett('O', O);
	             break;
	             case R.id.bP:
	                  gjett('P', P);
	             break;
	             case R.id.bQ:
	            	 gjett('Q', Q);
	             break;
	             case R.id.bR:
	            	 gjett('R', Rb);
	             break;
	             case R.id.bS:
	                  gjett('S', S);
	             break;
	             case R.id.bT:
	            	 gjett('T', T);
	             break;
	             case R.id.bU:
	            	 gjett('U', U);
	             break;
	             case R.id.bV:
	                  gjett('V', V);
	             break;
	             case R.id.bW:
	                  gjett('W', W);
	             break;
	             case R.id.bX:
	            	 gjett('X', X);
	             break;
	             case R.id.bY:
	            	 gjett('Y', Y);            	 
	             break;
	             case R.id.bZ:
	            	 gjett('Z', Z);
	             break;
	         }

	   }
		 
	};
	
	private void deklarerKnapper()
	{
		A = (Button) findViewById(R.id.bA);
		B = (Button) findViewById(R.id.bB);
		C = (Button) findViewById(R.id.bC);
		D = (Button) findViewById(R.id.bD);
		E = (Button) findViewById(R.id.bE);
		F = (Button) findViewById(R.id.bF);
		G = (Button) findViewById(R.id.bG);
		H = (Button) findViewById(R.id.bH);
		I = (Button) findViewById(R.id.bI);
		J = (Button) findViewById(R.id.bJ);
		K = (Button) findViewById(R.id.bK);
		L = (Button) findViewById(R.id.bL);
		M = (Button) findViewById(R.id.bM);
		N = (Button) findViewById(R.id.bN);
		O = (Button) findViewById(R.id.bO);
		P = (Button) findViewById(R.id.bP);
		Q = (Button) findViewById(R.id.bQ);
		Rb = (Button) findViewById(R.id.bR);
		S = (Button) findViewById(R.id.bS);
		T = (Button) findViewById(R.id.bT);
		U = (Button) findViewById(R.id.bU);
		V = (Button) findViewById(R.id.bV);
		W = (Button) findViewById(R.id.bW);
		X = (Button) findViewById(R.id.bX);
		Y = (Button) findViewById(R.id.bY);
		Z = (Button) findViewById(R.id.bZ);
		
		A.setOnClickListener(onClickListener);
		B.setOnClickListener(onClickListener);
		C.setOnClickListener(onClickListener);
		D.setOnClickListener(onClickListener);
		E.setOnClickListener(onClickListener);
		F.setOnClickListener(onClickListener);
		G.setOnClickListener(onClickListener);
		H.setOnClickListener(onClickListener);
		I.setOnClickListener(onClickListener);
		J.setOnClickListener(onClickListener);
		K.setOnClickListener(onClickListener);
		L.setOnClickListener(onClickListener);
		M.setOnClickListener(onClickListener);
		N.setOnClickListener(onClickListener);
		O.setOnClickListener(onClickListener);
		P.setOnClickListener(onClickListener);
		Q.setOnClickListener(onClickListener);
		Rb.setOnClickListener(onClickListener);
		S.setOnClickListener(onClickListener);
		T.setOnClickListener(onClickListener);
		U.setOnClickListener(onClickListener);
		V.setOnClickListener(onClickListener);
		W.setOnClickListener(onClickListener);
		X.setOnClickListener(onClickListener);
		Y.setOnClickListener(onClickListener);
		Z.setOnClickListener(onClickListener);
	}
	
	/*
	 * 
	 * Method to handle orientation switching. Executes several operations neccesary for the game to behave
	 * the way it should if the device and orientation is rotated.
	 */
	@Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.game);
		imageHangman = (ImageView)findViewById(R.id.imageHangman);
		imageHangman.setVisibility(View.VISIBLE);
		
		ordFelt = (TextView) findViewById(R.id.textRulesView);
		multiFelt = (TextView) findViewById(R.id.textMulti);
		poengFelt = (TextView) findViewById(R.id.textPoeng);

		ordFelt.setText(uferdigOrd);
		poengFelt.setText(String.valueOf(poeng));
		
		deklarerKnapper();
		tegnHangman();

		if(forrigeRett == true)
		{
			if(fForrigeRett==true)
			{
				visMultiplier("4x");
			}
			else
			{
				visMultiplier("2x");
			}
		}
		
		/* See comments above next method.
		 */
		tegnBrukteKnapper("A", A);
		tegnBrukteKnapper("B", B);
		tegnBrukteKnapper("C", C);
		tegnBrukteKnapper("D", D);
		tegnBrukteKnapper("E", E);
		tegnBrukteKnapper("F", F);
		tegnBrukteKnapper("G", G);
		tegnBrukteKnapper("H", H);
		tegnBrukteKnapper("I", I);
		tegnBrukteKnapper("J", J);
		tegnBrukteKnapper("K", K);
		tegnBrukteKnapper("L", L);
		tegnBrukteKnapper("M", M);
		tegnBrukteKnapper("N", N);
		tegnBrukteKnapper("O", O);
		tegnBrukteKnapper("P", P);
		tegnBrukteKnapper("Q", Q);
		tegnBrukteKnapper("R", Rb);
		tegnBrukteKnapper("S", S);
		tegnBrukteKnapper("T", T);
		tegnBrukteKnapper("U", U);
		tegnBrukteKnapper("V", V);
		tegnBrukteKnapper("W", W);
		tegnBrukteKnapper("X", X);
		tegnBrukteKnapper("Y", Y);
		tegnBrukteKnapper("Z", Z);
		
		
    }
	
	/* This method's only purpose is to check whether a button is already used, and update it on orientation change.
	 * I am really not proud of this way of keeping the buttons state on orientation change,
	 * but i did not really find a better way to do it before deadline. I would definitely change this if I was
	 * to develop it further.
	 */
	private void tegnBrukteKnapper(String st, Button bu)
	{	
			if(riktigBokstavString.contains(st))
			{		
				bu.setBackgroundResource(R.xml.rounded_green); bu.setEnabled(false);
			}	
			
			else if(feilBokstavString.contains(st))
			{
				bu.setBackgroundResource(R.xml.rounded_red); bu.setEnabled(false);
			}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.exitmain, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId())
		{
		case R.id.exit:
			avsluttAppDialog();
		break;
		case android.R.id.home:
			if(feilGjettTeller==0)
			{
				spillTeller--;
				globOrdTeller--;
				finish();
			}
			else
			{
				avsluttSpillDialog();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void avsluttAppDialog()
	{
		//Variabler
		dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setCancelable(false);
		
		//Process
		dialogBuilder.setTitle(getString(R.string.dialogAvsluttTittel));
		dialogBuilder.setMessage(getString(R.string.dialogAvsluttApp));
		
		dialogBuilder.setPositiveButton((getString(R.string.yes)), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				
				spillTaptTeller++;
				ordFeilTeller++;
				setResult(99);
				finish();
			}
		});
		
		dialogBuilder.setNegativeButton((getString(R.string.no)), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				
				dialog.dismiss();
			}
		});
		
		AlertDialog alert = dialogBuilder.create();
		alert.show();

	}
	
	private void feilOrdDialog()
	{
		//Variabler
		deaktiverKnapper();
		dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setCancelable(false);
		
		//Process
		ferdigOrd = ferdigOrd.replaceAll("\\s+","");
		dialogBuilder.setTitle(getString(R.string.beklager));
		dialogBuilder.setMessage(getString(R.string.beklagertekst) + " " + ferdigOrd + "\n" + getString(R.string.beklagertekst2));
		
		dialogBuilder.setPositiveButton((getString(R.string.yes)), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				
				ordTabell = new String[5];
				Arrays.fill(ordTabell, null);
				ordTeller = 0;
				poeng = 0;
				spillTeller++;
	    		imageHangman.setImageResource(R.drawable.h0);
	    		tilbakestillKnapper();
	    		poengFelt.setText(String.valueOf(poeng));
	    		nyttSpill();
			}
		});
		
		dialogBuilder.setNegativeButton((getString(R.string.no)), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
			}
		}).show();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
}
