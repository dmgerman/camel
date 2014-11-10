begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|html
operator|.
name|parser
operator|.
name|DTD
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Test JavadocParser using {@link java.lang.String} javadoc for Java 6, 7 and 8.  */
end_comment

begin_class
DECL|class|JavadocParserTest
specifier|public
class|class
name|JavadocParserTest
extends|extends
name|Assert
block|{
DECL|field|JAVA6_STRING
specifier|private
specifier|static
specifier|final
name|String
name|JAVA6_STRING
init|=
literal|"https://docs.oracle.com/javase/6/docs/api/java/lang/String.html"
decl_stmt|;
DECL|field|JAVA7_STRING
specifier|private
specifier|static
specifier|final
name|String
name|JAVA7_STRING
init|=
literal|"https://docs.oracle.com/javase/7/docs/api/java/lang/String.html"
decl_stmt|;
DECL|field|JAVA8_STRING
specifier|private
specifier|static
specifier|final
name|String
name|JAVA8_STRING
init|=
literal|"https://docs.oracle.com/javase/8/docs/api/java/lang/String.html"
decl_stmt|;
annotation|@
name|Test
DECL|method|testGetMethods ()
specifier|public
name|void
name|testGetMethods
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DTD
name|dtd
init|=
name|DTD
operator|.
name|getDTD
argument_list|(
literal|"html.dtd"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|javaDocPath
init|=
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|"/"
argument_list|)
operator|+
literal|".html"
decl_stmt|;
specifier|final
name|JavadocParser
name|htmlParser
init|=
operator|new
name|JavadocParser
argument_list|(
name|dtd
argument_list|,
name|javaDocPath
argument_list|)
decl_stmt|;
name|htmlParser
operator|.
name|parse
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|URL
argument_list|(
name|JAVA6_STRING
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Java6 getErrorMessage"
argument_list|,
name|htmlParser
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Java6 getMethods"
argument_list|,
name|htmlParser
operator|.
name|getMethods
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|htmlParser
operator|.
name|reset
argument_list|()
expr_stmt|;
name|htmlParser
operator|.
name|parse
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|URL
argument_list|(
name|JAVA7_STRING
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Java7 getErrorMessage"
argument_list|,
name|htmlParser
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Java7 getMethods"
argument_list|,
name|htmlParser
operator|.
name|getMethods
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|htmlParser
operator|.
name|reset
argument_list|()
expr_stmt|;
name|htmlParser
operator|.
name|parse
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|URL
argument_list|(
name|JAVA8_STRING
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Java8 getErrorMessage"
argument_list|,
name|htmlParser
operator|.
name|getErrorMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Java8 getMethods"
argument_list|,
name|htmlParser
operator|.
name|getMethods
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

