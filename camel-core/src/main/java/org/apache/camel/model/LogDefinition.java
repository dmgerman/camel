begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
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
name|annotation
operator|.
name|XmlAccessorType
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
name|annotation
operator|.
name|XmlAttribute
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
name|annotation
operator|.
name|XmlRootElement
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
name|Expression
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
name|LoggingLevel
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
name|Processor
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
name|processor
operator|.
name|LogProcessor
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
name|processor
operator|.
name|Logger
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
name|spi
operator|.
name|RouteContext
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;log/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"log"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|LogDefinition
specifier|public
class|class
name|LogDefinition
extends|extends
name|ProcessorDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|message
specifier|private
name|String
name|message
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|loggingLevel
specifier|private
name|LoggingLevel
name|loggingLevel
init|=
name|LoggingLevel
operator|.
name|INFO
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|logName
specifier|private
name|String
name|logName
decl_stmt|;
DECL|method|LogDefinition ()
specifier|public
name|LogDefinition
parameter_list|()
block|{     }
DECL|method|LogDefinition (String message)
specifier|public
name|LogDefinition
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Log["
operator|+
name|message
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"log"
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|message
argument_list|,
literal|"message"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// use simple language for the message string to give it more power
name|Expression
name|exp
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|createExpression
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|getLogName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
name|Logger
name|logger
init|=
operator|new
name|Logger
argument_list|(
name|name
argument_list|,
name|getLoggingLevel
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|LogProcessor
argument_list|(
name|exp
argument_list|,
name|logger
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addOutput (ProcessorDefinition output)
specifier|public
name|void
name|addOutput
parameter_list|(
name|ProcessorDefinition
name|output
parameter_list|)
block|{
comment|// add outputs on parent as this log does not support outputs
name|getParent
argument_list|()
operator|.
name|addOutput
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
DECL|method|getLoggingLevel ()
specifier|public
name|LoggingLevel
name|getLoggingLevel
parameter_list|()
block|{
return|return
name|loggingLevel
return|;
block|}
DECL|method|setLoggingLevel (LoggingLevel loggingLevel)
specifier|public
name|void
name|setLoggingLevel
parameter_list|(
name|LoggingLevel
name|loggingLevel
parameter_list|)
block|{
name|this
operator|.
name|loggingLevel
operator|=
name|loggingLevel
expr_stmt|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
DECL|method|setMessage (String message)
specifier|public
name|void
name|setMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
DECL|method|getLogName ()
specifier|public
name|String
name|getLogName
parameter_list|()
block|{
return|return
name|logName
return|;
block|}
DECL|method|setLogName (String logName)
specifier|public
name|void
name|setLogName
parameter_list|(
name|String
name|logName
parameter_list|)
block|{
name|this
operator|.
name|logName
operator|=
name|logName
expr_stmt|;
block|}
block|}
end_class

end_unit

