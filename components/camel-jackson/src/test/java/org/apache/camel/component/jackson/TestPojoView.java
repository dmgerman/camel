begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jackson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jackson
package|;
end_package

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|map
operator|.
name|annotate
operator|.
name|JsonView
import|;
end_import

begin_class
DECL|class|TestPojoView
specifier|public
class|class
name|TestPojoView
block|{
DECL|field|age
specifier|private
name|int
name|age
init|=
literal|30
decl_stmt|;
DECL|field|height
specifier|private
name|int
name|height
init|=
literal|190
decl_stmt|;
DECL|field|weight
specifier|private
name|int
name|weight
init|=
literal|70
decl_stmt|;
annotation|@
name|JsonView
argument_list|(
name|Views
operator|.
name|Age
operator|.
name|class
argument_list|)
DECL|method|getAge ()
specifier|public
name|int
name|getAge
parameter_list|()
block|{
return|return
name|age
return|;
block|}
DECL|method|setAge (int age)
specifier|public
name|void
name|setAge
parameter_list|(
name|int
name|age
parameter_list|)
block|{
name|this
operator|.
name|age
operator|=
name|age
expr_stmt|;
block|}
DECL|method|getHeight ()
specifier|public
name|int
name|getHeight
parameter_list|()
block|{
return|return
name|height
return|;
block|}
DECL|method|setHeight (int height)
specifier|public
name|void
name|setHeight
parameter_list|(
name|int
name|height
parameter_list|)
block|{
name|this
operator|.
name|height
operator|=
name|height
expr_stmt|;
block|}
annotation|@
name|JsonView
argument_list|(
name|Views
operator|.
name|Weight
operator|.
name|class
argument_list|)
DECL|method|getWeight ()
specifier|public
name|int
name|getWeight
parameter_list|()
block|{
return|return
name|weight
return|;
block|}
DECL|method|setWeight (int weight)
specifier|public
name|void
name|setWeight
parameter_list|(
name|int
name|weight
parameter_list|)
block|{
name|this
operator|.
name|weight
operator|=
name|weight
expr_stmt|;
block|}
block|}
end_class

end_unit

