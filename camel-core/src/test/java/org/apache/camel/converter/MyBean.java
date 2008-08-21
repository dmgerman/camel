begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
package|;
end_package

begin_class
DECL|class|MyBean
specifier|public
class|class
name|MyBean
block|{
DECL|field|foo
specifier|private
name|int
name|foo
decl_stmt|;
DECL|field|bar
specifier|private
name|String
name|bar
decl_stmt|;
DECL|method|MyBean ()
specifier|public
name|MyBean
parameter_list|()
block|{
name|this
argument_list|(
literal|0
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|MyBean (int foo, String bar)
specifier|public
name|MyBean
parameter_list|(
name|int
name|foo
parameter_list|,
name|String
name|bar
parameter_list|)
block|{
name|this
operator|.
name|foo
operator|=
name|foo
expr_stmt|;
name|this
operator|.
name|bar
operator|=
name|bar
expr_stmt|;
block|}
DECL|method|getFoo ()
specifier|public
name|int
name|getFoo
parameter_list|()
block|{
return|return
name|foo
return|;
block|}
DECL|method|setFoo (int foo)
specifier|public
name|void
name|setFoo
parameter_list|(
name|int
name|foo
parameter_list|)
block|{
name|this
operator|.
name|foo
operator|=
name|foo
expr_stmt|;
block|}
DECL|method|getBar ()
specifier|public
name|String
name|getBar
parameter_list|()
block|{
return|return
name|bar
return|;
block|}
DECL|method|setBar (String bar)
specifier|public
name|void
name|setBar
parameter_list|(
name|String
name|bar
parameter_list|)
block|{
name|this
operator|.
name|bar
operator|=
name|bar
expr_stmt|;
block|}
block|}
end_class

end_unit

