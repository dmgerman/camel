begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

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
name|Endpoint
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
name|UriEndpointComponent
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|MllpEndpoint
operator|.
name|END_OF_BLOCK
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|MllpEndpoint
operator|.
name|END_OF_DATA
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|MllpEndpoint
operator|.
name|START_OF_BLOCK
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link MllpEndpoint}.  */
end_comment

begin_class
DECL|class|MllpComponent
specifier|public
class|class
name|MllpComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|MLLP_LOG_PHI_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_LOG_PHI_PROPERTY
init|=
literal|"org.apache.camel.component.mllp.logPHI"
decl_stmt|;
DECL|method|MllpComponent ()
specifier|public
name|MllpComponent
parameter_list|()
block|{
name|super
argument_list|(
name|MllpEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|MllpComponent (CamelContext context)
specifier|public
name|MllpComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|MllpEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|MllpEndpoint
name|endpoint
init|=
operator|new
name|MllpEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// mllp://hostname:port
name|String
name|hostPort
decl_stmt|;
comment|// look for options
name|int
name|optionsStartIndex
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
if|if
condition|(
operator|-
literal|1
operator|==
name|optionsStartIndex
condition|)
block|{
comment|// No options - just get the host/port stuff
name|hostPort
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|7
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|hostPort
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|7
argument_list|,
name|optionsStartIndex
argument_list|)
expr_stmt|;
block|}
comment|// Make sure it has a host - may just be a port
name|int
name|colonIndex
init|=
name|hostPort
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
operator|-
literal|1
operator|!=
name|colonIndex
condition|)
block|{
name|endpoint
operator|.
name|setHostname
argument_list|(
name|hostPort
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|colonIndex
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setPort
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|hostPort
operator|.
name|substring
argument_list|(
name|colonIndex
operator|+
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// No host specified - leave the default host and set the port
name|endpoint
operator|.
name|setPort
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|hostPort
operator|.
name|substring
argument_list|(
name|colonIndex
operator|+
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|isLogPhi ()
specifier|public
specifier|static
name|boolean
name|isLogPhi
parameter_list|()
block|{
name|String
name|logPhiProperty
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|MllpComponent
operator|.
name|MLLP_LOG_PHI_PROPERTY
argument_list|,
literal|"true"
argument_list|)
decl_stmt|;
return|return
name|Boolean
operator|.
name|valueOf
argument_list|(
name|logPhiProperty
argument_list|)
return|;
block|}
DECL|method|covertToPrintFriendlyString (String hl7Message)
specifier|public
specifier|static
name|String
name|covertToPrintFriendlyString
parameter_list|(
name|String
name|hl7Message
parameter_list|)
block|{
if|if
condition|(
name|hl7Message
operator|==
literal|null
condition|)
block|{
return|return
literal|"null"
return|;
block|}
elseif|else
if|if
condition|(
name|hl7Message
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|"empty"
return|;
block|}
return|return
name|hl7Message
operator|.
name|replaceAll
argument_list|(
literal|""
operator|+
name|START_OF_BLOCK
argument_list|,
literal|"<VT>"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|""
operator|+
name|END_OF_BLOCK
argument_list|,
literal|"<FS>"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r"
argument_list|,
literal|"<CR>"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\n"
argument_list|,
literal|"<LF>"
argument_list|)
return|;
block|}
DECL|method|covertBytesToPrintFriendlyString (byte[] hl7Bytes)
specifier|public
specifier|static
name|String
name|covertBytesToPrintFriendlyString
parameter_list|(
name|byte
index|[]
name|hl7Bytes
parameter_list|)
block|{
if|if
condition|(
name|hl7Bytes
operator|==
literal|null
condition|)
block|{
return|return
literal|"null"
return|;
block|}
elseif|else
if|if
condition|(
name|hl7Bytes
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
name|covertBytesToPrintFriendlyString
argument_list|(
name|hl7Bytes
argument_list|,
literal|0
argument_list|,
name|hl7Bytes
operator|.
name|length
argument_list|)
return|;
block|}
DECL|method|covertBytesToPrintFriendlyString (byte[] hl7Bytes, int startPosition, int length)
specifier|public
specifier|static
name|String
name|covertBytesToPrintFriendlyString
parameter_list|(
name|byte
index|[]
name|hl7Bytes
parameter_list|,
name|int
name|startPosition
parameter_list|,
name|int
name|length
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|hl7Bytes
condition|)
block|{
return|return
literal|"null"
return|;
block|}
elseif|else
if|if
condition|(
name|hl7Bytes
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
name|covertToPrintFriendlyString
argument_list|(
operator|new
name|String
argument_list|(
name|hl7Bytes
argument_list|,
name|startPosition
argument_list|,
name|length
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

