begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.wsrm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|wsrm
package|;
end_package

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|HelloWorldImpl
specifier|public
class|class
name|HelloWorldImpl
implements|implements
name|HelloWorld
block|{
DECL|field|logger
specifier|private
specifier|static
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|HelloWorldImpl
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|sayHi (String text)
specifier|public
name|String
name|sayHi
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"***** Entering Implementation Code ******"
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"***** Leaving Implementation Code ******"
argument_list|)
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|text
return|;
block|}
block|}
end_class

end_unit

