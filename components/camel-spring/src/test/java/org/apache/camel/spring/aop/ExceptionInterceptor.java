begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.aop
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|aop
package|;
end_package

begin_class
DECL|class|ExceptionInterceptor
specifier|public
class|class
name|ExceptionInterceptor
block|{
DECL|field|e
specifier|private
name|Exception
name|e
decl_stmt|;
DECL|method|logException (Exception e)
specifier|public
name|void
name|logException
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|e
operator|=
name|e
expr_stmt|;
block|}
DECL|method|getE ()
specifier|public
name|Exception
name|getE
parameter_list|()
block|{
return|return
name|e
return|;
block|}
block|}
end_class

end_unit

