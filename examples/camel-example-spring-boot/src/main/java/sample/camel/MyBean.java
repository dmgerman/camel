begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|sample.camel
package|package
name|sample
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Value
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
name|Component
import|;
end_import

begin_comment
comment|/**  * A bean that returns a message when you call the {@link #saySomething()} method.  *<p/>  * Uses<tt>@Component("myBean")</tt> to register this bean with the name<tt>myBean</tt>  * that we use in the Camel route to lookup this bean.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"myBean"
argument_list|)
DECL|class|MyBean
specifier|public
class|class
name|MyBean
block|{
annotation|@
name|Value
argument_list|(
literal|"${greeting}"
argument_list|)
DECL|field|say
specifier|private
name|String
name|say
decl_stmt|;
DECL|method|saySomething ()
specifier|public
name|String
name|saySomething
parameter_list|()
block|{
return|return
name|say
return|;
block|}
block|}
end_class

end_unit

