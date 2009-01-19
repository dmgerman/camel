begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
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
name|ExchangePattern
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
name|Message
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
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfExchange
specifier|public
class|class
name|CxfExchange
extends|extends
name|DefaultExchange
block|{
comment|/**      * @param cxfEndpoint      * @param pattern      */
DECL|method|CxfExchange (CxfEndpoint cxfEndpoint, ExchangePattern pattern)
specifier|public
name|CxfExchange
parameter_list|(
name|CxfEndpoint
name|cxfEndpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|super
argument_list|(
name|cxfEndpoint
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createInMessage ()
specifier|protected
name|Message
name|createInMessage
parameter_list|()
block|{
return|return
operator|new
name|CxfMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createOutMessage ()
specifier|protected
name|Message
name|createOutMessage
parameter_list|()
block|{
return|return
operator|new
name|CxfMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createFaultMessage ()
specifier|protected
name|Message
name|createFaultMessage
parameter_list|()
block|{
return|return
operator|new
name|CxfMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

