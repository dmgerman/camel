begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
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
name|impl
operator|.
name|DefaultHeaderFilterStrategy
import|;
end_import

begin_comment
comment|/**  * Default header filtering strategy for Restlet  *   * @version   */
end_comment

begin_class
DECL|class|RestletHeaderFilterStrategy
specifier|public
class|class
name|RestletHeaderFilterStrategy
extends|extends
name|DefaultHeaderFilterStrategy
block|{
DECL|method|RestletHeaderFilterStrategy ()
specifier|public
name|RestletHeaderFilterStrategy
parameter_list|()
block|{
comment|// No IN filters and copy all headers from Restlet to Camel
comment|// OUT filters (from Camel headers to Restlet headers)
comment|// filter headers used internally by this component
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|RestletConstants
operator|.
name|RESTLET_LOGIN
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|RestletConstants
operator|.
name|RESTLET_PASSWORD
argument_list|)
expr_stmt|;
comment|// The "CamelAcceptContentType" header is not added to the outgoing HTTP
comment|// headers but it will be going out as "Accept.
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|Exchange
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

