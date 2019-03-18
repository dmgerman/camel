begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser.java
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|java
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|MySimpleRouteBuilder
specifier|public
class|class
name|MySimpleRouteBuilder
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"timer:foo"
argument_list|)
operator|.
name|filter
argument_list|(
name|simple
argument_list|(
literal|"${body}> 100"
argument_list|)
argument_list|)
operator|.
name|toD
argument_list|(
literal|"log:a"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|filter
argument_list|()
operator|.
name|simple
argument_list|(
literal|"${body}> 200"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:b"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:c"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

