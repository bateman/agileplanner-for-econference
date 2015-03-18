

# How to build agileplanner-for-econference plug-in #

## Build the eConference 3.x ##
Follow the [instructions](http://code.google.com/p/econference/wiki/HowToBuildEConf) to build eConference 3.x

## Build the econference-planning-poker-plugin 1.x ##
Follow the [instructions](http://code.google.com/p/econference-planning-poker-plugin/wiki/HowToBuildEConf3P#How_to_build_eConference_Planning_Poker_Plug-in_1.x) to build econference-planning-poker-plugin 1.x

## Checkout the SVN repository ##

After that, open the _Window | Open perspective_ menu (see Fig. 3)...

<p align='center'><a href='http://econference.googlecode.com/svn/wiki/img/svn2.JPG'>http://econference.googlecode.com/svn/wiki/img/svn2.JPG</a> <br>
<p align='center'><b>Figure 3. Display all the available perspectives</b></p>
<br>
<br>

...and select <i>SVN Repository Exploring</i> (see Fig. 4)<br>
<br>
<p align='center'><a href='http://econference.googlecode.com/svn/wiki/img/svn3.JPG'>http://econference.googlecode.com/svn/wiki/img/svn3.JPG</a> <br>
<p align='center'><b>Figure 4. Add the SVN Repository Exploring perpective to the workbench</b></p>
<br>
<br>

After switching to the <i>SVN Repository Exploring</i> perspective, add a new repository as shown below (Fig. 5).<br>
<br>
<p align='center'><a href='http://econference.googlecode.com/svn/wiki/img/svn4.JPG'>http://econference.googlecode.com/svn/wiki/img/svn4.JPG</a> <br>
<p align='center'><b>Figure 5. Add the SVN Repository Location</b></p>
<br>
<br>

Now, enter <code>http://agileplanner-for-econference.googlecode.com/svn/trunk/</code> in the url field of the wizard and click the <i>Finish</i> button. Accept the Digital Certificate when prompted.<br>
<br>
<br>

Once Subclipse has finished fetching data from the new repository, you will see the repository name show up in the tree table viewer on the left pane. Browse <i>Trunk</i>, then select all the plugins and right-click and choose <i>Checkout...</i> from the menu.<br>
<br>
A 2-steps wizard will guide you. You are going to checkout each plugin as a separate project into your workspace. It could take a while, so be patient.<br>
<br>
<h2>Launching the product</h2>

Once check-out is finished, right click on <code>it.uniba.di.cdg.planningpoker.boot.product</code> file in <code>it.uniba.di.cdg.planningpoker</code> package, and select <i>Run</i> | <i>Run configurations...</i>. to include <b>agileplanner-for-econference</b> plug-in into eConference product.<br>
<br>
<br>
Select the current product configuration under the Eclipse Application (Fig. 6, A) and shift on <i>Plug-ins tab</i> (B); then select <code>ca.ucalgary.cpsc.agilePlanner (2.5.0)</code> plug-in (C). Now press the Validate Plug-in Set button (D). If an error message pops up, then close it and press the Add Required Plug-ins button (E); otherwise here you are already ok (and the problem should be somewhere else :S).<br>
<br>
<p align='center'><a href='http://econference.googlecode.com/svn/wiki/img/runconfAP.JPG'>http://econference.googlecode.com/svn/wiki/img/runconfAP.JPG</a> <br>
<p align='center'><b>Figure 6. Validate the plug-ins set</b></p>
<br>
<br>

From now on, you can just use the tool into eConference 3.x. To start using the tool, you can just click on icon button of eConference perspective(Fig. 7).<br>
<br>
<p align='center'><a href='http://econference.googlecode.com/svn/wiki/img/econfpersp.JPG'>http://econference.googlecode.com/svn/wiki/img/econfpersp.JPG</a> <br>
<p align='center'><b>Figure 7. eConference perspective with AgilePlanner plug-in</b></p>
<br>
<br>


<h2>How to export plug-in</h2>

In order to use this plug-in out of the environment of eConference (e.g., as pure Eclipse plug-in), you can export product in a .jar file. To do this, you can right click on <code>AgilePlannerWithPersister.product</code> file in <code>it.uniba.di.cdg.agilePlanner</code> package e then select <i>Export</i> (Fig. 8):<br>
<br>
<p align='center'><a href='http://econference.googlecode.com/svn/wiki/img/exp1AP.JPG'>http://econference.googlecode.com/svn/wiki/img/exp1AP.JPG</a> <br>
<p align='center'><b>Figure 8. Export AgilePlannerWithPersister.product</b></p>
<br>
<br>

A 2-steps wizard will guide you. In the Export frame, select <i>Plug-in Development | Deployable plug-ins and fragments</i>, then click <i>Next</i> (Fig. 9):<br>
<br>
<p align='center'><a href='http://econference.googlecode.com/svn/wiki/img/exp2AP.JPG'>http://econference.googlecode.com/svn/wiki/img/exp2AP.JPG</a> <br>
<p align='center'><b>Figure 9. Export plug-in as deployable plug-in</b></p>
<br>
<br>

Select <code>ca.ucalgary.cpsc.agilePlanner (2.5.0)</code> from Avaiable Plug-ins and Fragments frame (Fig. 10, A), then enter destination directory you want to save .jar file (B). Click <i>Finish</i>:<br>
<br>
<p align='center'><a href='http://econference.googlecode.com/svn/wiki/img/exp3AP.JPG'>http://econference.googlecode.com/svn/wiki/img/exp3AP.JPG</a> <br>
<p align='center'><b>Figure 10. Select plug-in and enter destination directory</b></p>
<br>
<br>

At the end of export process, you will have .jar file in your destination directory.