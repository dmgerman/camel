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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|spi
operator|.
name|Metadata
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

begin_comment
comment|/**  * Logs the defined message to the logger  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,configuration"
argument_list|)
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
name|NoOutputDefinition
argument_list|<
name|LogDefinition
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LogDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|message
specifier|private
name|String
name|message
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"INFO"
argument_list|)
DECL|field|loggingLevel
specifier|private
name|LoggingLevel
name|loggingLevel
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|logName
specifier|private
name|String
name|logName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|marker
specifier|private
name|String
name|marker
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|loggerRef
specifier|private
name|String
name|loggerRef
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|logger
specifier|private
name|Logger
name|logger
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
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"log"
return|;
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
comment|/**      * Sets the logging level.      *<p/>      * The default value is INFO      */
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
comment|/**      * Sets the log message (uses simple language)      */
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
comment|/**      * Sets the name of the logger      */
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
DECL|method|getMarker ()
specifier|public
name|String
name|getMarker
parameter_list|()
block|{
return|return
name|marker
return|;
block|}
comment|/**      * To use slf4j marker      */
DECL|method|setMarker (String marker)
specifier|public
name|void
name|setMarker
parameter_list|(
name|String
name|marker
parameter_list|)
block|{
name|this
operator|.
name|marker
operator|=
name|marker
expr_stmt|;
block|}
DECL|method|getLoggerRef ()
specifier|public
name|String
name|getLoggerRef
parameter_list|()
block|{
return|return
name|loggerRef
return|;
block|}
comment|/**      * To refer to a custom logger instance to lookup from the registry.      */
DECL|method|setLoggerRef (String loggerRef)
specifier|public
name|void
name|setLoggerRef
parameter_list|(
name|String
name|loggerRef
parameter_list|)
block|{
name|this
operator|.
name|loggerRef
operator|=
name|loggerRef
expr_stmt|;
block|}
DECL|method|getLogger ()
specifier|public
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
comment|/**      * To use a custom logger instance      */
DECL|method|setLogger (Logger logger)
specifier|public
name|void
name|setLogger
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
block|}
end_class

end_unit
