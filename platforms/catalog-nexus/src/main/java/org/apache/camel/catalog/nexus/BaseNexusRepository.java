begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.nexus
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|nexus
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|Set
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
name|Executors
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
name|ScheduledExecutorService
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
name|TimeUnit
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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|catalog
operator|.
name|CamelCatalog
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_class
DECL|class|BaseNexusRepository
specifier|public
specifier|abstract
class|class
name|BaseNexusRepository
block|{
DECL|field|log
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|indexedArtifacts
specifier|private
specifier|final
name|Set
argument_list|<
name|NexusArtifactDto
argument_list|>
name|indexedArtifacts
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|executorService
specifier|private
specifier|volatile
name|ScheduledExecutorService
name|executorService
decl_stmt|;
DECL|field|started
specifier|private
name|AtomicBoolean
name|started
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
DECL|field|camelCatalog
specifier|private
name|CamelCatalog
name|camelCatalog
decl_stmt|;
DECL|field|initialDelay
specifier|private
name|int
name|initialDelay
init|=
literal|10
decl_stmt|;
DECL|field|delay
specifier|private
name|int
name|delay
init|=
literal|60
decl_stmt|;
DECL|field|nexusUrl
specifier|private
name|String
name|nexusUrl
init|=
literal|"http://nexus/service/local/data_index"
decl_stmt|;
DECL|field|classifier
specifier|private
name|String
name|classifier
decl_stmt|;
DECL|method|BaseNexusRepository (String classifier)
specifier|public
name|BaseNexusRepository
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
DECL|method|getCamelCatalog ()
specifier|public
name|CamelCatalog
name|getCamelCatalog
parameter_list|()
block|{
return|return
name|camelCatalog
return|;
block|}
DECL|method|setCamelCatalog (CamelCatalog camelCatalog)
specifier|public
name|void
name|setCamelCatalog
parameter_list|(
name|CamelCatalog
name|camelCatalog
parameter_list|)
block|{
name|this
operator|.
name|camelCatalog
operator|=
name|camelCatalog
expr_stmt|;
block|}
DECL|method|getNexusUrl ()
specifier|public
name|String
name|getNexusUrl
parameter_list|()
block|{
return|return
name|nexusUrl
return|;
block|}
comment|/**      * The URL to the Nexus repository to query. The syntax should be<tt>http://nexus/service/local/data_index</tt>, where      * nexus is the hostname.      */
DECL|method|setNexusUrl (String nexusUrl)
specifier|public
name|void
name|setNexusUrl
parameter_list|(
name|String
name|nexusUrl
parameter_list|)
block|{
name|this
operator|.
name|nexusUrl
operator|=
name|nexusUrl
expr_stmt|;
block|}
DECL|method|getInitialDelay ()
specifier|public
name|int
name|getInitialDelay
parameter_list|()
block|{
return|return
name|initialDelay
return|;
block|}
comment|/**      * Delay in seconds before the initial (first) scan.      */
DECL|method|setInitialDelay (int initialDelay)
specifier|public
name|void
name|setInitialDelay
parameter_list|(
name|int
name|initialDelay
parameter_list|)
block|{
name|this
operator|.
name|initialDelay
operator|=
name|initialDelay
expr_stmt|;
block|}
DECL|method|getDelay ()
specifier|public
name|int
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
comment|/**      * Delay in seconds between scanning.      */
DECL|method|setDelay (int delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|int
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
DECL|method|getClassifier ()
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|classifier
return|;
block|}
comment|/**      * Classifier to index. Should be either<tt>component</tt>, or<tt>connector</tt>      */
DECL|method|setClassifier (String classifier)
specifier|public
name|void
name|setClassifier
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
comment|/**      * Starts the Nexus indexer.      */
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|camelCatalog
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelCatalog must be configured"
argument_list|)
throw|;
block|}
if|if
condition|(
name|nexusUrl
operator|==
literal|null
operator|||
name|nexusUrl
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Nexus service not found. Indexing Nexus is not enabled!"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|started
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"NexusRepository is already started"
argument_list|)
expr_stmt|;
return|return;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Starting NexusRepository to scan every {} seconds"
argument_list|,
name|delay
argument_list|)
expr_stmt|;
name|executorService
operator|=
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|scheduleWithFixedDelay
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Indexing Nexus {} +++ start +++"
argument_list|,
name|nexusUrl
argument_list|)
expr_stmt|;
name|indexNexus
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"UnknownHostException"
argument_list|)
condition|)
block|{
comment|// less noise if its unknown host
name|log
operator|.
name|warn
argument_list|(
literal|"Error indexing Nexus "
operator|+
name|nexusUrl
operator|+
literal|" due unknown hosts: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error indexing Nexus "
operator|+
name|nexusUrl
operator|+
literal|" due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Indexing Nexus {} +++ end +++"
argument_list|,
name|nexusUrl
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
name|initialDelay
argument_list|,
name|delay
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Stops the Nexus indexer.      */
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Stopping NexusRepository"
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
name|indexedArtifacts
operator|.
name|clear
argument_list|()
expr_stmt|;
name|started
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Callback when new artifacts has been discovered in Nexus      */
DECL|method|onNewArtifacts (Set<NexusArtifactDto> newArtifacts)
specifier|abstract
name|void
name|onNewArtifacts
parameter_list|(
name|Set
argument_list|<
name|NexusArtifactDto
argument_list|>
name|newArtifacts
parameter_list|)
function_decl|;
DECL|method|createNexusUrl ()
specifier|protected
name|URL
name|createNexusUrl
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|String
name|query
init|=
name|nexusUrl
operator|+
literal|"?q="
operator|+
name|getClassifier
argument_list|()
decl_stmt|;
return|return
operator|new
name|URL
argument_list|(
name|query
argument_list|)
return|;
block|}
comment|/**      * Runs the task to index nexus for new artifacts      */
DECL|method|indexNexus ()
specifier|protected
name|void
name|indexNexus
parameter_list|()
throws|throws
name|Exception
block|{
comment|// must have q parameter so use component to find all component
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setIgnoringElementContentWhitespace
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setIgnoringComments
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|documentBuilder
init|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|createNexusUrl
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|Document
name|dom
init|=
name|documentBuilder
operator|.
name|parse
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|XPathFactory
name|xpFactory
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|XPath
name|exp
init|=
name|xpFactory
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|NodeList
name|list
init|=
operator|(
name|NodeList
operator|)
name|exp
operator|.
name|evaluate
argument_list|(
literal|"//data/artifact"
argument_list|,
name|dom
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|NexusArtifactDto
argument_list|>
name|newArtifacts
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|g
init|=
name|getNodeText
argument_list|(
name|node
operator|.
name|getChildNodes
argument_list|()
argument_list|,
literal|"groupId"
argument_list|)
decl_stmt|;
name|String
name|a
init|=
name|getNodeText
argument_list|(
name|node
operator|.
name|getChildNodes
argument_list|()
argument_list|,
literal|"artifactId"
argument_list|)
decl_stmt|;
name|String
name|v
init|=
name|getNodeText
argument_list|(
name|node
operator|.
name|getChildNodes
argument_list|()
argument_list|,
literal|"version"
argument_list|)
decl_stmt|;
name|String
name|l
init|=
name|getNodeText
argument_list|(
name|node
operator|.
name|getChildNodes
argument_list|()
argument_list|,
literal|"artifactLink"
argument_list|)
decl_stmt|;
if|if
condition|(
name|g
operator|!=
literal|null
operator|&
name|a
operator|!=
literal|null
operator|&
name|v
operator|!=
literal|null
operator|&
name|l
operator|!=
literal|null
condition|)
block|{
name|NexusArtifactDto
name|dto
init|=
operator|new
name|NexusArtifactDto
argument_list|()
decl_stmt|;
name|dto
operator|.
name|setGroupId
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|dto
operator|.
name|setArtifactId
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|dto
operator|.
name|setVersion
argument_list|(
name|v
argument_list|)
expr_stmt|;
name|dto
operator|.
name|setArtifactLink
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Found: {}:{}:{}"
argument_list|,
name|dto
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|dto
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|dto
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
comment|// is it a new artifact
name|boolean
name|newArtifact
init|=
literal|true
decl_stmt|;
for|for
control|(
name|NexusArtifactDto
name|existing
range|:
name|indexedArtifacts
control|)
block|{
if|if
condition|(
name|existing
operator|.
name|getGroupId
argument_list|()
operator|.
name|equals
argument_list|(
name|dto
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|&&
name|existing
operator|.
name|getArtifactId
argument_list|()
operator|.
name|equals
argument_list|(
name|dto
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|&&
name|existing
operator|.
name|getVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|dto
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|newArtifact
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|newArtifact
condition|)
block|{
name|newArtifacts
operator|.
name|add
argument_list|(
name|dto
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// if there is any new artifacts then process them
if|if
condition|(
operator|!
name|newArtifacts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|onNewArtifacts
argument_list|(
name|newArtifacts
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getNodeText (NodeList list, String name)
specifier|private
specifier|static
name|String
name|getNodeText
parameter_list|(
name|NodeList
name|list
parameter_list|,
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|child
operator|.
name|getTextContent
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|close (Closeable... closeables)
specifier|private
specifier|static
name|void
name|close
parameter_list|(
name|Closeable
modifier|...
name|closeables
parameter_list|)
block|{
for|for
control|(
name|Closeable
name|c
range|:
name|closeables
control|)
block|{
try|try
block|{
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
end_class

end_unit

