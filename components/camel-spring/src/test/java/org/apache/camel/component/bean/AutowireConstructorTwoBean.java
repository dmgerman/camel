begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|Autowired
import|;
end_import

begin_comment
comment|/**  * This bean has both a default no-arg and a autowired constructor  */
end_comment

begin_class
DECL|class|AutowireConstructorTwoBean
specifier|public
class|class
name|AutowireConstructorTwoBean
block|{
DECL|field|counter
specifier|private
specifier|final
name|MyBeanCounter
name|counter
decl_stmt|;
DECL|method|AutowireConstructorTwoBean ()
specifier|public
name|AutowireConstructorTwoBean
parameter_list|()
block|{
name|this
operator|.
name|counter
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|AutowireConstructorTwoBean (@utowired MyBeanCounter counter)
specifier|public
name|AutowireConstructorTwoBean
parameter_list|(
annotation|@
name|Autowired
name|MyBeanCounter
name|counter
parameter_list|)
block|{
name|this
operator|.
name|counter
operator|=
name|counter
expr_stmt|;
block|}
DECL|method|hello (String name)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|name
operator|+
literal|" at "
operator|+
name|counter
operator|.
name|count
argument_list|()
return|;
block|}
block|}
end_class

end_unit

