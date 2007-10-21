begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|ConduitSelector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
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
name|cxf
operator|.
name|message
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Conduit
import|;
end_import

begin_class
DECL|class|NullConduitSelector
specifier|public
class|class
name|NullConduitSelector
implements|implements
name|ConduitSelector
block|{
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|nullConduit
specifier|private
name|NullConduit
name|nullConduit
decl_stmt|;
DECL|method|NullConduitSelector ()
specifier|public
name|NullConduitSelector
parameter_list|()
block|{
name|nullConduit
operator|=
operator|new
name|NullConduit
argument_list|()
expr_stmt|;
block|}
DECL|method|complete (Exchange exchange)
specifier|public
name|void
name|complete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|//do nothing here
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|prepare (Message message)
specifier|public
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
comment|//do nothing here
block|}
DECL|method|selectConduit (Message message)
specifier|public
name|Conduit
name|selectConduit
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|nullConduit
return|;
block|}
DECL|method|setEndpoint (Endpoint endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
block|}
end_class

end_unit

