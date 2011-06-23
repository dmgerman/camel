begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jt400
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jt400
package|;
end_package

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyVetoException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|AS400
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|DataQueue
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
name|CamelException
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
name|PollingConsumer
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
name|Producer
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
name|impl
operator|.
name|DefaultPollingEndpoint
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
comment|/**  * AS/400 Data queue endpoint  */
end_comment

begin_class
DECL|class|Jt400DataQueueEndpoint
specifier|public
class|class
name|Jt400DataQueueEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
comment|/**      * Enumeration of supported data formats      */
DECL|enum|Format
specifier|public
enum|enum
name|Format
block|{
comment|/**          * Using<code>String</code> for transferring data          */
DECL|enumConstant|text
name|text
block|,
comment|/**          * Using<code>byte[]</code> for transferring data          */
DECL|enumConstant|binary
name|binary
block|;     }
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Jt400DataQueueEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|system
specifier|private
specifier|final
name|AS400
name|system
decl_stmt|;
DECL|field|objectPath
specifier|private
specifier|final
name|String
name|objectPath
decl_stmt|;
DECL|field|dataqueue
specifier|private
name|DataQueue
name|dataqueue
decl_stmt|;
DECL|field|format
specifier|private
name|Format
name|format
init|=
name|Format
operator|.
name|text
decl_stmt|;
comment|/**      * Creates a new AS/400 data queue endpoint      */
DECL|method|Jt400DataQueueEndpoint (String endpointUri, Jt400Component component)
specifier|protected
name|Jt400DataQueueEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Jt400Component
name|component
parameter_list|)
throws|throws
name|CamelException
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
try|try
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
name|String
index|[]
name|credentials
init|=
name|uri
operator|.
name|getUserInfo
argument_list|()
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|system
operator|=
operator|new
name|AS400
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|,
name|credentials
index|[
literal|0
index|]
argument_list|,
name|credentials
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|objectPath
operator|=
name|uri
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Unable to parse URI for "
operator|+
name|endpointUri
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|system
operator|.
name|setGuiAvailable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PropertyVetoException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed do disable AS/400 prompting in the environment running Camel."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setCcsid (int ccsid)
specifier|public
name|void
name|setCcsid
parameter_list|(
name|int
name|ccsid
parameter_list|)
throws|throws
name|PropertyVetoException
block|{
name|this
operator|.
name|system
operator|.
name|setCcsid
argument_list|(
name|ccsid
argument_list|)
expr_stmt|;
block|}
DECL|method|setFormat (Format format)
specifier|public
name|void
name|setFormat
parameter_list|(
name|Format
name|format
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
DECL|method|getFormat ()
specifier|public
name|Format
name|getFormat
parameter_list|()
block|{
return|return
name|format
return|;
block|}
DECL|method|setGuiAvailable (boolean guiAvailable)
specifier|public
name|void
name|setGuiAvailable
parameter_list|(
name|boolean
name|guiAvailable
parameter_list|)
throws|throws
name|PropertyVetoException
block|{
name|this
operator|.
name|system
operator|.
name|setGuiAvailable
argument_list|(
name|guiAvailable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|Jt400DataQueueConsumer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|Jt400DataQueueProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|getSystem ()
specifier|protected
name|AS400
name|getSystem
parameter_list|()
block|{
return|return
name|system
return|;
block|}
DECL|method|getDataQueue ()
specifier|protected
name|DataQueue
name|getDataQueue
parameter_list|()
block|{
if|if
condition|(
name|dataqueue
operator|==
literal|null
condition|)
block|{
name|dataqueue
operator|=
operator|new
name|DataQueue
argument_list|(
name|system
argument_list|,
name|objectPath
argument_list|)
expr_stmt|;
block|}
return|return
name|dataqueue
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

