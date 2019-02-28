begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|Producer
import|;
end_import

begin_comment
comment|/**  * Used for components that can optimise the usage of {@link org.apache.camel.processor.SendDynamicProcessor} (toD)  * to reuse a static {@link org.apache.camel.Endpoint} and {@link Producer} that supports  * using headers to provide the dynamic parts. For example many of the HTTP components supports this.  */
end_comment

begin_interface
DECL|interface|SendDynamicAware
specifier|public
interface|interface
name|SendDynamicAware
block|{
comment|/**      * Sets the component name.      *      * @param scheme  name of the component      */
DECL|method|setScheme (String scheme)
name|void
name|setScheme
parameter_list|(
name|String
name|scheme
parameter_list|)
function_decl|;
comment|/**      * Gets the component name      */
DECL|method|getScheme ()
name|String
name|getScheme
parameter_list|()
function_decl|;
comment|/**      * An entry of detailed information from the recipient uri, which allows the {@link SendDynamicAware}      * implementation to prepare pre- and post- processor and the static uri to be used for the optimised dynamic to.      */
DECL|class|DynamicAwareEntry
class|class
name|DynamicAwareEntry
block|{
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
DECL|field|originalUri
specifier|private
specifier|final
name|String
name|originalUri
decl_stmt|;
DECL|field|properties
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
decl_stmt|;
DECL|field|lenientProperties
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|lenientProperties
decl_stmt|;
DECL|method|DynamicAwareEntry (String uri, String originalUri, Map<String, String> properties, Map<String, String> lenientProperties)
specifier|public
name|DynamicAwareEntry
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|originalUri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|lenientProperties
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|originalUri
operator|=
name|originalUri
expr_stmt|;
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
name|this
operator|.
name|lenientProperties
operator|=
name|lenientProperties
expr_stmt|;
block|}
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|getOriginalUri ()
specifier|public
name|String
name|getOriginalUri
parameter_list|()
block|{
return|return
name|originalUri
return|;
block|}
DECL|method|getProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|getLenientProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getLenientProperties
parameter_list|()
block|{
return|return
name|lenientProperties
return|;
block|}
block|}
comment|/**      * Prepares for using optimised dynamic to by parsing the uri and returning an entry of details that are      * used for creating the pre and post processors, and the static uri.      *      * @param exchange     the exchange      * @param uri          the resolved uri which is intended to be used      * @param originalUri  the original uri of the endpoint before any dynamic evaluation      * @return prepared information about the dynamic endpoint to use      * @throws Exception is thrown if error parsing the uri      */
DECL|method|prepare (Exchange exchange, String uri, String originalUri)
name|DynamicAwareEntry
name|prepare
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|originalUri
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Resolves the static part of the uri that are used for creating a single {@link org.apache.camel.Endpoint}      * and {@link Producer} that will be reused for processing the optimised toD.      *      * @param exchange    the exchange      * @param entry       prepared information about the dynamic endpoint to use      * @return the static uri, or<tt>null</tt> to not let toD use this optimisation.      * @throws Exception is thrown if error resolving the static uri.      */
DECL|method|resolveStaticUri (Exchange exchange, DynamicAwareEntry entry)
name|String
name|resolveStaticUri
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|DynamicAwareEntry
name|entry
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Creates the pre {@link Processor} that will prepare the {@link Exchange}      * with dynamic details from the given recipient.      *      * @param exchange    the exchange      * @param entry       prepared information about the dynamic endpoint to use      * @return the processor, or<tt>null</tt> to not let toD use this optimisation.      * @throws Exception is thrown if error creating the pre processor.      */
DECL|method|createPreProcessor (Exchange exchange, DynamicAwareEntry entry)
name|Processor
name|createPreProcessor
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|DynamicAwareEntry
name|entry
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Creates an optional post {@link Processor} that will be executed afterwards      * when the message has been sent dynamic.      *      * @param exchange    the exchange      * @param entry       prepared information about the dynamic endpoint to use      * @return the post processor, or<tt>null</tt> if no post processor is needed.      * @throws Exception is thrown if error creating the post processor.      */
DECL|method|createPostProcessor (Exchange exchange, DynamicAwareEntry entry)
name|Processor
name|createPostProcessor
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|DynamicAwareEntry
name|entry
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

