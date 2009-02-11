begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jhc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jhc
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
name|impl
operator|.
name|DefaultHeaderFilterStrategy
import|;
end_import

begin_comment
comment|/**  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|JhcHeaderFilterStrategy
specifier|public
class|class
name|JhcHeaderFilterStrategy
extends|extends
name|DefaultHeaderFilterStrategy
block|{
DECL|method|JhcHeaderFilterStrategy ()
specifier|public
name|JhcHeaderFilterStrategy
parameter_list|()
block|{
name|initialize
argument_list|()
expr_stmt|;
block|}
DECL|method|initialize ()
specifier|protected
name|void
name|initialize
parameter_list|()
block|{
comment|// We could import filters from http component but that also means
comment|// a new dependency on camel-http
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"content-length"
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"content-type"
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|JhcProducer
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
expr_stmt|;
name|setLowercase
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// filter headers begin with "org.apache.camel"
name|setOutFilterPattern
argument_list|(
literal|"(org\\.apache\\.camel)[\\.|a-z|A-z|0-9]*"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

