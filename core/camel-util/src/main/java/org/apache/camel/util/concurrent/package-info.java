begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * The JAXB POJOs for the  *<a href="http://camel.apache.org/xml-configuration.html">XML Configuration</a>  * of the routing rules.  */
end_comment

begin_annotation
annotation|@
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlSchema
argument_list|(
name|namespace
operator|=
literal|"http://camel.apache.org/schema/spring"
argument_list|,
name|elementFormDefault
operator|=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlNsForm
operator|.
name|QUALIFIED
argument_list|)
end_annotation

begin_package
DECL|package|org.apache.camel.util.concurrent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|concurrent
package|;
end_package

end_unit

