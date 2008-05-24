begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_comment
comment|/**  * Holder object for a given value.  */
end_comment

begin_class
DECL|class|ValueHolder
specifier|public
class|class
name|ValueHolder
parameter_list|<
name|V
parameter_list|>
block|{
DECL|field|value
specifier|private
name|V
name|value
decl_stmt|;
DECL|method|ValueHolder ()
specifier|public
name|ValueHolder
parameter_list|()
block|{     }
DECL|method|ValueHolder (V val)
specifier|public
name|ValueHolder
parameter_list|(
name|V
name|val
parameter_list|)
block|{
name|value
operator|=
name|val
expr_stmt|;
block|}
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|set (V val)
specifier|public
name|void
name|set
parameter_list|(
name|V
name|val
parameter_list|)
block|{
name|value
operator|=
name|val
expr_stmt|;
block|}
block|}
end_class

end_unit

