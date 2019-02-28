begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ProducerTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IOHelper
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
name|ApplicationContext
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
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|FileSystemXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * A command line tool for booting up a CamelContext using an optional Spring  * {@link org.springframework.context.ApplicationContext}.  *<p/>  * By placing a file in the {@link #LOCATION_PROPERTIES} directory of any JARs on the classpath,  * allows this Main class to load those additional Spring XML files as Spring  * {@link org.springframework.context.ApplicationContext} to be included.  *<p/>  * Each line in the {@link #LOCATION_PROPERTIES} is a reference to a Spring XML file to include,  * which by default gets loaded from classpath.  */
end_comment

begin_class
DECL|class|Main
specifier|public
class|class
name|Main
extends|extends
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
operator|.
name|MainSupport
block|{
DECL|field|LOCATION_PROPERTIES
specifier|public
specifier|static
specifier|final
name|String
name|LOCATION_PROPERTIES
init|=
literal|"META-INF/camel-spring/location.properties"
decl_stmt|;
DECL|field|instance
specifier|protected
specifier|static
name|Main
name|instance
decl_stmt|;
DECL|field|UTF8
specifier|private
specifier|static
specifier|final
name|Charset
name|UTF8
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
DECL|field|applicationContextUri
specifier|private
name|String
name|applicationContextUri
init|=
literal|"META-INF/spring/*.xml"
decl_stmt|;
DECL|field|fileApplicationContextUri
specifier|private
name|String
name|fileApplicationContextUri
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|AbstractApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|parentApplicationContext
specifier|private
name|AbstractApplicationContext
name|parentApplicationContext
decl_stmt|;
DECL|field|additionalApplicationContext
specifier|private
name|AbstractApplicationContext
name|additionalApplicationContext
decl_stmt|;
DECL|field|parentApplicationContextUri
specifier|private
name|String
name|parentApplicationContextUri
decl_stmt|;
DECL|method|Main ()
specifier|public
name|Main
parameter_list|()
block|{
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"ac"
argument_list|,
literal|"applicationContext"
argument_list|,
literal|"Sets the classpath based spring ApplicationContext"
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
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"fa"
argument_list|,
literal|"fileApplicationContext"
argument_list|,
literal|"Sets the filesystem based spring ApplicationContext"
argument_list|,
literal|"fileApplicationContext"
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
name|setFileApplicationContextUri
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|main (String... args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|instance
operator|=
name|main
expr_stmt|;
name|main
operator|.
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the currently executing main      *      * @return the current running instance      */
DECL|method|getInstance ()
specifier|public
specifier|static
name|Main
name|getInstance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
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
DECL|method|getFileApplicationContextUri ()
specifier|public
name|String
name|getFileApplicationContextUri
parameter_list|()
block|{
return|return
name|fileApplicationContextUri
return|;
block|}
DECL|method|setFileApplicationContextUri (String fileApplicationContextUri)
specifier|public
name|void
name|setFileApplicationContextUri
parameter_list|(
name|String
name|fileApplicationContextUri
parameter_list|)
block|{
name|this
operator|.
name|fileApplicationContextUri
operator|=
name|fileApplicationContextUri
expr_stmt|;
block|}
DECL|method|getParentApplicationContext ()
specifier|public
name|AbstractApplicationContext
name|getParentApplicationContext
parameter_list|()
block|{
if|if
condition|(
name|parentApplicationContext
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|parentApplicationContextUri
operator|!=
literal|null
condition|)
block|{
name|parentApplicationContext
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
name|parentApplicationContextUri
argument_list|)
expr_stmt|;
name|parentApplicationContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|parentApplicationContext
return|;
block|}
DECL|method|setParentApplicationContext (AbstractApplicationContext parentApplicationContext)
specifier|public
name|void
name|setParentApplicationContext
parameter_list|(
name|AbstractApplicationContext
name|parentApplicationContext
parameter_list|)
block|{
name|this
operator|.
name|parentApplicationContext
operator|=
name|parentApplicationContext
expr_stmt|;
block|}
DECL|method|getParentApplicationContextUri ()
specifier|public
name|String
name|getParentApplicationContextUri
parameter_list|()
block|{
return|return
name|parentApplicationContextUri
return|;
block|}
DECL|method|setParentApplicationContextUri (String parentApplicationContextUri)
specifier|public
name|void
name|setParentApplicationContextUri
parameter_list|(
name|String
name|parentApplicationContextUri
parameter_list|)
block|{
name|this
operator|.
name|parentApplicationContextUri
operator|=
name|parentApplicationContextUri
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|SpringCamelContext
argument_list|>
name|camels
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|SpringCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|camels
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Multiple CamelContext detected. This Main class only supports single CamelContext"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|camels
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|camels
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|super
operator|.
name|doStart
argument_list|()
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
comment|// then start any additional after Camel has been started
if|if
condition|(
name|additionalApplicationContext
operator|==
literal|null
condition|)
block|{
name|additionalApplicationContext
operator|=
name|createAdditionalLocationsFromClasspath
argument_list|()
expr_stmt|;
if|if
condition|(
name|additionalApplicationContext
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting Additional ApplicationContext: {}"
argument_list|,
name|additionalApplicationContext
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|additionalApplicationContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting Spring ApplicationContext: {}"
argument_list|,
name|applicationContext
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|applicationContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|initCamelContext
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// if we were veto started then mark as completed
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
operator|&&
name|getCamelContext
argument_list|()
operator|.
name|isVetoStarted
argument_list|()
condition|)
block|{
name|completed
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|additionalApplicationContext
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping Additional ApplicationContext: {}"
argument_list|,
name|additionalApplicationContext
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|additionalApplicationContext
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping Spring ApplicationContext: {}"
argument_list|,
name|applicationContext
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|findOrCreateCamelTemplate ()
specifier|protected
name|ProducerTemplate
name|findOrCreateCamelTemplate
parameter_list|()
block|{
name|String
index|[]
name|names
init|=
name|getApplicationContext
argument_list|()
operator|.
name|getBeanNamesForType
argument_list|(
name|ProducerTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|!=
literal|null
operator|&&
name|names
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
name|names
index|[
literal|0
index|]
argument_list|,
name|ProducerTemplate
operator|.
name|class
argument_list|)
return|;
block|}
if|if
condition|(
name|getCamelContext
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No CamelContext are available so cannot create a ProducerTemplate!"
argument_list|)
throw|;
block|}
return|return
name|getCamelContext
argument_list|()
operator|.
name|createProducerTemplate
argument_list|()
return|;
block|}
DECL|method|createDefaultApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createDefaultApplicationContext
parameter_list|()
throws|throws
name|IOException
block|{
name|ApplicationContext
name|parentContext
init|=
name|getParentApplicationContext
argument_list|()
decl_stmt|;
comment|// file based
if|if
condition|(
name|getFileApplicationContextUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|args
init|=
name|getFileApplicationContextUri
argument_list|()
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentContext
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|FileSystemXmlApplicationContext
argument_list|(
name|args
argument_list|,
name|parentContext
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|FileSystemXmlApplicationContext
argument_list|(
name|args
argument_list|)
return|;
block|}
block|}
comment|// default to classpath based
name|String
index|[]
name|args
init|=
name|getApplicationContextUri
argument_list|()
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentContext
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
name|args
argument_list|,
name|parentContext
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
name|args
argument_list|)
return|;
block|}
block|}
DECL|method|createAdditionalLocationsFromClasspath ()
specifier|protected
name|AbstractApplicationContext
name|createAdditionalLocationsFromClasspath
parameter_list|()
throws|throws
name|IOException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|locations
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|findLocations
argument_list|(
name|locations
argument_list|,
name|Main
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|locations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Found locations for additional Spring XML files: {}"
argument_list|,
name|locations
argument_list|)
expr_stmt|;
name|String
index|[]
name|locs
init|=
name|locations
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|locations
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
name|locs
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|findLocations (Set<String> locations, ClassLoader classLoader)
specifier|protected
name|void
name|findLocations
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|locations
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|IOException
block|{
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|resources
init|=
name|classLoader
operator|.
name|getResources
argument_list|(
name|LOCATION_PROPERTIES
argument_list|)
decl_stmt|;
while|while
condition|(
name|resources
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
name|resources
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|BufferedReader
name|reader
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|,
name|UTF8
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
operator|||
name|line
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
name|locations
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|reader
argument_list|,
literal|null
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

