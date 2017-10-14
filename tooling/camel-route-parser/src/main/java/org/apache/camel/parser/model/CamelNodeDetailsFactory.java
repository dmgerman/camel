begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|model
package|;
end_package

begin_class
DECL|class|CamelNodeDetailsFactory
specifier|public
specifier|final
class|class
name|CamelNodeDetailsFactory
block|{
DECL|field|order
specifier|private
name|int
name|order
decl_stmt|;
DECL|method|CamelNodeDetailsFactory ()
specifier|private
name|CamelNodeDetailsFactory
parameter_list|()
block|{     }
DECL|method|newInstance ()
specifier|public
specifier|static
name|CamelNodeDetailsFactory
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|CamelNodeDetailsFactory
argument_list|()
return|;
block|}
DECL|method|newNode (CamelNodeDetails parent, String name)
specifier|public
name|CamelNodeDetails
name|newNode
parameter_list|(
name|CamelNodeDetails
name|parent
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|CamelNodeDetails
argument_list|(
name|parent
argument_list|,
name|name
argument_list|,
operator|++
name|order
argument_list|)
return|;
block|}
DECL|method|copyNode (CamelNodeDetails parent, String name, CamelNodeDetails copoy)
specifier|public
name|CamelNodeDetails
name|copyNode
parameter_list|(
name|CamelNodeDetails
name|parent
parameter_list|,
name|String
name|name
parameter_list|,
name|CamelNodeDetails
name|copoy
parameter_list|)
block|{
return|return
operator|new
name|CamelNodeDetails
argument_list|(
name|parent
argument_list|,
name|name
argument_list|,
operator|++
name|order
argument_list|,
name|copoy
argument_list|)
return|;
block|}
block|}
end_class

end_unit

