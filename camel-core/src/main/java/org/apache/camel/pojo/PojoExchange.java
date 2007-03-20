begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.pojo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|pojo
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
name|DefaultExchange
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

begin_comment
comment|/**  * @version $Revision: 519901 $  */
end_comment

begin_class
DECL|class|PojoExchange
specifier|public
class|class
name|PojoExchange
extends|extends
name|DefaultExchange
argument_list|<
name|PojoInvocation
argument_list|,
name|Object
argument_list|,
name|Throwable
argument_list|>
block|{
DECL|method|PojoExchange (CamelContext container)
specifier|public
name|PojoExchange
parameter_list|(
name|CamelContext
name|container
parameter_list|)
block|{
name|super
argument_list|(
name|container
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

