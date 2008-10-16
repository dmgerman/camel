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
name|util
operator|.
name|HashMap
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|spring
operator|.
name|handler
operator|.
name|CamelNamespaceHandler
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
name|MainSupport
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
name|view
operator|.
name|ModelFileGenerator
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
comment|/**  * A command line tool for booting up a CamelContext using an optional Spring  * ApplicationContext  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|Main
specifier|public
class|class
name|Main
extends|extends
name|MainSupport
block|{
DECL|field|instance
specifier|private
specifier|static
name|Main
name|instance
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
literal|"a"
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
annotation|@
name|Override
DECL|method|enableDebug ()
specifier|public
name|void
name|enableDebug
parameter_list|()
block|{
name|super
operator|.
name|enableDebug
argument_list|()
expr_stmt|;
name|setParentApplicationContextUri
argument_list|(
literal|"/META-INF/services/org/apache/camel/spring/debug.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|enableTrace ()
specifier|public
name|void
name|enableTrace
parameter_list|()
block|{
comment|// TODO
name|super
operator|.
name|enableTrace
argument_list|()
expr_stmt|;
name|setParentApplicationContextUri
argument_list|(
literal|"/META-INF/services/org/apache/camel/spring/trace.xml"
argument_list|)
expr_stmt|;
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
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
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
name|applicationContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|postProcessContext
argument_list|()
expr_stmt|;
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
operator|(
name|ProducerTemplate
operator|)
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
for|for
control|(
name|CamelContext
name|camelContext
range|:
name|getCamelContexts
argument_list|()
control|)
block|{
return|return
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No CamelContexts are available so cannot create a ProducerTemplate!"
argument_list|)
throw|;
block|}
DECL|method|createDefaultApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createDefaultApplicationContext
parameter_list|()
block|{
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
name|ApplicationContext
name|parentContext
init|=
name|getParentApplicationContext
argument_list|()
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
name|ApplicationContext
name|parentContext
init|=
name|getParentApplicationContext
argument_list|()
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
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
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
DECL|method|getCamelContextMap ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
name|getCamelContextMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|SpringCamelContext
argument_list|>
name|map
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
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|SpringCamelContext
argument_list|>
argument_list|>
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|SpringCamelContext
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|CamelContext
name|camelContext
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createModelFileGenerator ()
specifier|protected
name|ModelFileGenerator
name|createModelFileGenerator
parameter_list|()
throws|throws
name|JAXBException
block|{
return|return
operator|new
name|ModelFileGenerator
argument_list|(
operator|new
name|CamelNamespaceHandler
argument_list|()
operator|.
name|getJaxbContext
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

