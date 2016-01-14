begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored.template.ast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
package|;
end_package

begin_class
DECL|class|OutParameter
specifier|public
class|class
name|OutParameter
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|sqlType
specifier|private
name|int
name|sqlType
decl_stmt|;
DECL|field|outValueMapKey
specifier|private
name|String
name|outValueMapKey
decl_stmt|;
DECL|method|OutParameter (String name, int sqlType, String outValueMapKey)
specifier|public
name|OutParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|sqlType
parameter_list|,
name|String
name|outValueMapKey
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|sqlType
operator|=
name|sqlType
expr_stmt|;
name|this
operator|.
name|outValueMapKey
operator|=
name|outValueMapKey
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getSqlType ()
specifier|public
name|int
name|getSqlType
parameter_list|()
block|{
return|return
name|sqlType
return|;
block|}
DECL|method|getOutValueMapKey ()
specifier|public
name|String
name|getOutValueMapKey
parameter_list|()
block|{
return|return
name|outValueMapKey
return|;
block|}
block|}
end_class

end_unit

