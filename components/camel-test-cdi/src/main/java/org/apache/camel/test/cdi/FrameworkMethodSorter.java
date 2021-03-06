begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|FrameworkMethod
import|;
end_import

begin_class
DECL|class|FrameworkMethodSorter
specifier|final
class|class
name|FrameworkMethodSorter
implements|implements
name|Comparator
argument_list|<
name|FrameworkMethod
argument_list|>
block|{
annotation|@
name|Override
DECL|method|compare (FrameworkMethod m1, FrameworkMethod m2)
specifier|public
name|int
name|compare
parameter_list|(
name|FrameworkMethod
name|m1
parameter_list|,
name|FrameworkMethod
name|m2
parameter_list|)
block|{
name|int
name|i1
init|=
literal|0
decl_stmt|;
name|int
name|i2
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|m1
operator|.
name|getMethod
argument_list|()
operator|.
name|isAnnotationPresent
argument_list|(
name|Order
operator|.
name|class
argument_list|)
condition|)
block|{
name|i1
operator|=
name|m1
operator|.
name|getMethod
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|Order
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|m2
operator|.
name|getMethod
argument_list|()
operator|.
name|isAnnotationPresent
argument_list|(
name|Order
operator|.
name|class
argument_list|)
condition|)
block|{
name|i2
operator|=
name|m2
operator|.
name|getMethod
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|Order
operator|.
name|class
argument_list|)
operator|.
name|value
argument_list|()
expr_stmt|;
block|}
return|return
name|i1
operator|<
name|i2
condition|?
operator|-
literal|1
else|:
operator|(
name|i1
operator|==
name|i2
condition|?
literal|0
else|:
literal|1
operator|)
return|;
block|}
block|}
end_class

end_unit

