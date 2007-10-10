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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
DECL|class|UnsafeCharactersEncoderTest
specifier|public
class|class
name|UnsafeCharactersEncoderTest
extends|extends
name|TestCase
block|{
DECL|method|testQnameEncoder ()
specifier|public
name|void
name|testQnameEncoder
parameter_list|()
block|{
name|String
name|afterEncoding
init|=
literal|"%7Bhttp://www.example.com/test%7DServiceName"
decl_stmt|;
name|String
name|beforeEncoding
init|=
literal|"{http://www.example.com/test}ServiceName"
decl_stmt|;
name|String
name|result
init|=
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|beforeEncoding
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong encoding result"
argument_list|,
name|result
argument_list|,
name|afterEncoding
argument_list|)
expr_stmt|;
block|}
DECL|method|testNoEncoding ()
specifier|public
name|void
name|testNoEncoding
parameter_list|()
block|{
name|String
name|noEncoding
init|=
literal|"http://www.example.com"
decl_stmt|;
name|String
name|result
init|=
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|noEncoding
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong encoding result"
argument_list|,
name|result
argument_list|,
name|noEncoding
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

