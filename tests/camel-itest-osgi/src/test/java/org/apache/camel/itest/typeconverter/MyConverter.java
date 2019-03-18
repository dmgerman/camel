begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.typeconverter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|typeconverter
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
name|Converter
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
name|itest
operator|.
name|Pojo
import|;
end_import

begin_class
annotation|@
name|Converter
DECL|class|MyConverter
specifier|public
class|class
name|MyConverter
block|{
DECL|method|MyConverter ()
specifier|public
name|MyConverter
parameter_list|()
block|{ }
annotation|@
name|Converter
DECL|method|toPojo (String name)
specifier|public
specifier|static
name|Pojo
name|toPojo
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Pojo
name|pojo
init|=
operator|new
name|Pojo
argument_list|()
decl_stmt|;
name|pojo
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|pojo
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (Pojo pojo)
specifier|public
name|String
name|toString
parameter_list|(
name|Pojo
name|pojo
parameter_list|)
block|{
return|return
name|pojo
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

