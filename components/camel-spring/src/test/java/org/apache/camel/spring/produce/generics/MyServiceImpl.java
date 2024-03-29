begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.produce.generics
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|produce
operator|.
name|generics
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
name|Consume
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_class
annotation|@
name|Service
DECL|class|MyServiceImpl
specifier|public
class|class
name|MyServiceImpl
extends|extends
name|MyServiceSupport
argument_list|<
name|Double
argument_list|>
block|{
annotation|@
name|Consume
argument_list|(
literal|"direct:myService"
argument_list|)
annotation|@
name|Override
DECL|method|sqrt (Double number)
specifier|public
name|Double
name|sqrt
parameter_list|(
name|Double
name|number
parameter_list|)
block|{
name|log
argument_list|(
name|number
argument_list|)
expr_stmt|;
return|return
name|Math
operator|.
name|sqrt
argument_list|(
name|number
argument_list|)
return|;
block|}
block|}
end_class

end_unit

