begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|ServiceSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_comment
comment|/**  * A command line tool for booting up a CamelContext using an  * optional Spring ApplicationContext  *  * @version $Revision: $  */
end_comment

begin_class
DECL|class|Main
specifier|public
class|class
name|Main
extends|extends
name|ServiceSupport
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|Main
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|applicationContextUri
specifier|private
name|String
name|applicationContextUri
init|=
literal|"META-INF/spring/*.xml"
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|AbstractApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|options
specifier|private
name|List
argument_list|<
name|Option
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|Option
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|latch
specifier|private
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|completed
specifier|private
name|AtomicBoolean
name|completed
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
DECL|method|Main ()
specifier|public
name|Main
parameter_list|()
block|{
name|addOption
argument_list|(
operator|new
name|Option
argument_list|(
literal|"h"
argument_list|,
literal|"help"
argument_list|,
literal|"Displays the help screen"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|showOptions
argument_list|()
expr_stmt|;
name|completed
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"a"
argument_list|,
literal|"applicationContext"
argument_list|,
literal|"Sets the classpath based pring ApplicationContext"
argument_list|,
literal|"applicationContext"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|setApplicationContextUri
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Parses the command line arguments then runs the program      */
DECL|method|run (String[] args)
specifier|public
name|void
name|run
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|parseArguments
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|run
argument_list|()
expr_stmt|;
block|}
comment|/**      * Runs this process with the given arguments      */
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
operator|!
name|completed
operator|.
name|get
argument_list|()
condition|)
block|{
try|try
block|{
name|start
argument_list|()
expr_stmt|;
name|waitUntilCompleted
argument_list|()
expr_stmt|;
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Marks this process as being completed      */
DECL|method|completed ()
specifier|public
name|void
name|completed
parameter_list|()
block|{
name|completed
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
comment|/**      * Displays the command line options      */
DECL|method|showOptions ()
specifier|public
name|void
name|showOptions
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Apache Camel Runner takes the following options"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
for|for
control|(
name|Option
name|option
range|:
name|options
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|option
operator|.
name|getAbbreviation
argument_list|()
operator|+
literal|" or "
operator|+
name|option
operator|.
name|getFullName
argument_list|()
operator|+
literal|" = "
operator|+
name|option
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Parses the commandl ine arguments      */
DECL|method|parseArguments (String[] arguments)
specifier|public
name|void
name|parseArguments
parameter_list|(
name|String
index|[]
name|arguments
parameter_list|)
block|{
name|LinkedList
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|arguments
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|valid
init|=
literal|true
decl_stmt|;
while|while
condition|(
operator|!
name|args
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|arg
init|=
name|args
operator|.
name|removeFirst
argument_list|()
decl_stmt|;
name|boolean
name|handled
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Option
name|option
range|:
name|options
control|)
block|{
if|if
condition|(
name|option
operator|.
name|processOption
argument_list|(
name|arg
argument_list|,
name|args
argument_list|)
condition|)
block|{
name|handled
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|handled
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Unknown option: "
operator|+
name|arg
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|valid
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|valid
condition|)
block|{
name|showOptions
argument_list|()
expr_stmt|;
name|completed
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|addOption (Option option)
specifier|public
name|void
name|addOption
parameter_list|(
name|Option
name|option
parameter_list|)
block|{
name|options
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
DECL|class|Option
specifier|public
specifier|abstract
class|class
name|Option
block|{
DECL|field|abbreviation
specifier|private
name|String
name|abbreviation
decl_stmt|;
DECL|field|fullName
specifier|private
name|String
name|fullName
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|method|Option (String abbreviation, String fullName, String description)
specifier|protected
name|Option
parameter_list|(
name|String
name|abbreviation
parameter_list|,
name|String
name|fullName
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|abbreviation
operator|=
literal|"-"
operator|+
name|abbreviation
expr_stmt|;
name|this
operator|.
name|fullName
operator|=
literal|"-"
operator|+
name|fullName
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|processOption (String arg, LinkedList<String> remainingArgs)
specifier|public
name|boolean
name|processOption
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
if|if
condition|(
name|arg
operator|.
name|equalsIgnoreCase
argument_list|(
name|abbreviation
argument_list|)
operator|||
name|fullName
operator|.
name|startsWith
argument_list|(
name|arg
argument_list|)
condition|)
block|{
name|doProcess
argument_list|(
name|arg
argument_list|,
name|remainingArgs
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|getAbbreviation ()
specifier|public
name|String
name|getAbbreviation
parameter_list|()
block|{
return|return
name|abbreviation
return|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|getFullName ()
specifier|public
name|String
name|getFullName
parameter_list|()
block|{
return|return
name|fullName
return|;
block|}
DECL|method|doProcess (String arg, LinkedList<String> remainingArgs)
specifier|protected
specifier|abstract
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
function_decl|;
block|}
DECL|class|ParameterOption
specifier|public
specifier|abstract
class|class
name|ParameterOption
extends|extends
name|Option
block|{
DECL|field|parameterName
specifier|private
name|String
name|parameterName
decl_stmt|;
DECL|method|ParameterOption (String abbreviation, String fullName, String description, String parameterName)
specifier|protected
name|ParameterOption
parameter_list|(
name|String
name|abbreviation
parameter_list|,
name|String
name|fullName
parameter_list|,
name|String
name|description
parameter_list|,
name|String
name|parameterName
parameter_list|)
block|{
name|super
argument_list|(
name|abbreviation
argument_list|,
name|fullName
argument_list|,
name|description
argument_list|)
expr_stmt|;
name|this
operator|.
name|parameterName
operator|=
name|parameterName
expr_stmt|;
block|}
DECL|method|doProcess (String arg, LinkedList<String> remainingArgs)
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
if|if
condition|(
name|remainingArgs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Expected fileName for "
argument_list|)
expr_stmt|;
name|showOptions
argument_list|()
expr_stmt|;
name|completed
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|parameter
init|=
name|remainingArgs
operator|.
name|removeFirst
argument_list|()
decl_stmt|;
name|doProcess
argument_list|(
name|arg
argument_list|,
name|parameter
argument_list|,
name|remainingArgs
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doProcess (String arg, String parameter, LinkedList<String> remainingArgs)
specifier|protected
specifier|abstract
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
function_decl|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getApplicationContext ()
specifier|public
name|AbstractApplicationContext
name|getApplicationContext
parameter_list|()
block|{
return|return
name|applicationContext
return|;
block|}
DECL|method|setApplicationContext (AbstractApplicationContext applicationContext)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|AbstractApplicationContext
name|applicationContext
parameter_list|)
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
DECL|method|getApplicationContextUri ()
specifier|public
name|String
name|getApplicationContextUri
parameter_list|()
block|{
return|return
name|applicationContextUri
return|;
block|}
DECL|method|setApplicationContextUri (String applicationContextUri)
specifier|public
name|void
name|setApplicationContextUri
parameter_list|(
name|String
name|applicationContextUri
parameter_list|)
block|{
name|this
operator|.
name|applicationContextUri
operator|=
name|applicationContextUri
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Apache Camel "
operator|+
name|getVersion
argument_list|()
operator|+
literal|" starting"
argument_list|)
expr_stmt|;
if|if
condition|(
name|applicationContext
operator|==
literal|null
condition|)
block|{
name|applicationContext
operator|=
name|createDefaultApplicationContext
argument_list|()
expr_stmt|;
block|}
name|applicationContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|createDefaultApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createDefaultApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
name|getApplicationContextUri
argument_list|()
argument_list|)
return|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Apache Camel terminating"
argument_list|)
expr_stmt|;
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|applicationContext
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|waitUntilCompleted ()
specifier|protected
name|void
name|waitUntilCompleted
parameter_list|()
block|{
while|while
condition|(
operator|!
name|completed
operator|.
name|get
argument_list|()
condition|)
block|{
try|try
block|{
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
DECL|method|getVersion ()
specifier|protected
name|String
name|getVersion
parameter_list|()
block|{
name|Package
name|aPackage
init|=
name|Package
operator|.
name|getPackage
argument_list|(
literal|"org.apache.camel"
argument_list|)
decl_stmt|;
if|if
condition|(
name|aPackage
operator|!=
literal|null
condition|)
block|{
name|String
name|version
init|=
name|aPackage
operator|.
name|getImplementationVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
name|version
operator|=
name|aPackage
operator|.
name|getSpecificationVersion
argument_list|()
expr_stmt|;
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
name|version
operator|=
literal|""
expr_stmt|;
block|}
block|}
return|return
name|version
return|;
block|}
return|return
literal|""
return|;
block|}
block|}
end_class

end_unit

