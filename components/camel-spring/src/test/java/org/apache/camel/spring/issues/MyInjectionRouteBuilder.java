begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|issues
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|MyInjectionRouteBuilder
specifier|public
class|class
name|MyInjectionRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|startEndpointUri
specifier|private
name|String
name|startEndpointUri
decl_stmt|;
DECL|field|myProcessor
specifier|private
name|Processor
name|myProcessor
decl_stmt|;
DECL|method|setStartEndpointUri (String startUri)
specifier|public
name|void
name|setStartEndpointUri
parameter_list|(
name|String
name|startUri
parameter_list|)
block|{
name|startEndpointUri
operator|=
name|startUri
expr_stmt|;
block|}
DECL|method|getStartEndpointUri ()
specifier|public
name|String
name|getStartEndpointUri
parameter_list|()
block|{
return|return
name|startEndpointUri
return|;
block|}
DECL|method|setMyProcessor (Processor processor)
specifier|public
name|void
name|setMyProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|myProcessor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|getMyProcessor ()
specifier|public
name|Processor
name|getMyProcessor
parameter_list|()
block|{
return|return
name|myProcessor
return|;
block|}
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|getStartEndpointUri
argument_list|()
argument_list|)
operator|.
name|process
argument_list|(
name|getMyProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

