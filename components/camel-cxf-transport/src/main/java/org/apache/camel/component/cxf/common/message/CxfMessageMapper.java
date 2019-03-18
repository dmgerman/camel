begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.common.message
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
name|common
operator|.
name|message
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
name|Exchange
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
name|HeaderFilterStrategy
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

begin_comment
comment|/**  * A Strategy to bind a Camel exchange to a CXF message used by {@link CxfBeanDestination}.  */
end_comment

begin_interface
DECL|interface|CxfMessageMapper
specifier|public
interface|interface
name|CxfMessageMapper
block|{
comment|/**      * Create a CXF {@link Message} from a Camel exchange.      */
DECL|method|createCxfMessageFromCamelExchange (Exchange camelExchange, HeaderFilterStrategy headerFilterStrategy)
name|Message
name|createCxfMessageFromCamelExchange
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|,
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
function_decl|;
comment|/**      * Given a CXF out/response Message, this method propagates response headers to a      * Camel exchange.      */
DECL|method|propagateResponseHeadersToCamel (Message cxfMessage, Exchange camelExchange, HeaderFilterStrategy headerFilterStrategy)
name|void
name|propagateResponseHeadersToCamel
parameter_list|(
name|Message
name|cxfMessage
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|,
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

