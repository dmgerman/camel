begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|HttpRouteUsingUrlPostTest
specifier|public
class|class
name|HttpRouteUsingUrlPostTest
extends|extends
name|HttpRouteTest
block|{
DECL|method|invokeHttpEndpoint ()
specifier|protected
name|void
name|invokeHttpEndpoint
parameter_list|()
throws|throws
name|IOException
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:8080/test"
argument_list|)
decl_stmt|;
name|URLConnection
name|urlConnection
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|urlConnection
operator|.
name|setDoInput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|urlConnection
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|urlConnection
operator|.
name|setUseCaches
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|urlConnection
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
comment|// Send POST data
name|OutputStream
name|out
init|=
name|urlConnection
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|BufferedWriter
name|writer
init|=
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|)
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// read the response data
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|urlConnection
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Read: "
operator|+
name|line
argument_list|)
expr_stmt|;
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

