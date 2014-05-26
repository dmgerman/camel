begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
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
name|jaxrs
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
name|component
operator|.
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
comment|/**  *  * @version   */
end_comment

begin_class
DECL|class|CxfRsHeaderFilterStrategy
specifier|public
class|class
name|CxfRsHeaderFilterStrategy
extends|extends
name|DefaultHeaderFilterStrategy
block|{
DECL|method|CxfRsHeaderFilterStrategy ()
specifier|public
name|CxfRsHeaderFilterStrategy
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
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|getOutFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"Content-Type"
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
comment|// Support to filter the Content-Type case insensitive
name|setLowerCase
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// filter headers begin with "Camel" or "org.apache.camel"
name|setOutFilterPattern
argument_list|(
literal|"(Camel|org\\.apache\\.camel)[\\.|a-z|A-z|0-9]*"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

