begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi.pojo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|pojo
package|;
end_package

begin_class
DECL|class|TypeConverterOutput
specifier|public
class|class
name|TypeConverterOutput
block|{
DECL|field|property
specifier|private
name|String
name|property
decl_stmt|;
DECL|method|getProperty ()
specifier|public
name|String
name|getProperty
parameter_list|()
block|{
return|return
name|property
return|;
block|}
DECL|method|setProperty (String property)
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|property
parameter_list|)
block|{
name|this
operator|.
name|property
operator|=
name|property
expr_stmt|;
block|}
block|}
end_class

end_unit

