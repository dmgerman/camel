begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Before
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TestSupport
operator|.
name|getJavaMajorVersion
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|XMLTokenExpressionIteratorTest
specifier|public
class|class
name|XMLTokenExpressionIteratorTest
extends|extends
name|Assert
block|{
DECL|field|TEST_BODY
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|TEST_BODY
init|=
operator|(
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle/><aunt>emma</aunt>"
operator|+
literal|"<c:parent some_attr='1' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='a' anotherAttr='a'></c:child>"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b'/>"
operator|+
literal|"</c:parent>"
operator|+
literal|"<c:parent some_attr='2' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='c' anotherAttr='c'></c:child>"
operator|+
literal|"<c:child some_attr='d' anotherAttr='d'/>"
operator|+
literal|"</c:parent>"
operator|+
literal|"</grandparent>"
operator|+
literal|"<grandparent><uncle>ben</uncle><aunt/>"
operator|+
literal|"<c:parent some_attr='3' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='e' anotherAttr='e'></c:child>"
operator|+
literal|"<c:child some_attr='f' anotherAttr='f'/>"
operator|+
literal|"</c:parent>"
operator|+
literal|"</grandparent>"
operator|+
literal|"</g:greatgrandparent>"
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
comment|// mixing a default namespace with an explicit namespace for child
DECL|field|TEST_BODY_NS_MIXED
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|TEST_BODY_NS_MIXED
init|=
operator|(
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent>"
operator|+
literal|"<parent some_attr='1' xmlns:c='urn:c' xmlns=\"urn:c\">"
operator|+
literal|"<child some_attr='a' anotherAttr='a'></child>"
operator|+
literal|"<x:child xmlns:x='urn:c' some_attr='b' anotherAttr='b'/>"
operator|+
literal|"</parent>"
operator|+
literal|"<c:parent some_attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<child some_attr='c' anotherAttr='c' xmlns='urn:c'></child>"
operator|+
literal|"<c:child some_attr='d' anotherAttr='d'/>"
operator|+
literal|"</c:parent>"
operator|+
literal|"</grandparent>"
operator|+
literal|"</g:greatgrandparent>"
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
comment|// mixing a no namespace with an explicit namespace for child
DECL|field|TEST_BODY_NO_NS_MIXED
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|TEST_BODY_NO_NS_MIXED
init|=
operator|(
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent>"
operator|+
literal|"<parent some_attr='1' xmlns:c='urn:c' xmlns=\"urn:c\">"
operator|+
literal|"<child some_attr='a' anotherAttr='a' xmlns=''></child>"
operator|+
literal|"<x:child xmlns:x='urn:c' some_attr='b' anotherAttr='b'/>"
operator|+
literal|"</parent>"
operator|+
literal|"<c:parent some_attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<child some_attr='c' anotherAttr='c'></child>"
operator|+
literal|"<c:child some_attr='d' anotherAttr='d'/>"
operator|+
literal|"</c:parent>"
operator|+
literal|"</grandparent>"
operator|+
literal|"</g:greatgrandparent>"
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
DECL|field|RESULTS_CHILD_WRAPPED
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_CHILD_WRAPPED
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle/><aunt>emma</aunt>"
operator|+
literal|"<c:parent some_attr='1' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='a' anotherAttr='a'></c:child>"
operator|+
literal|"</c:parent></grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle/><aunt>emma</aunt>"
operator|+
literal|"<c:parent some_attr='1' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b'/>"
operator|+
literal|"</c:parent></grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle/><aunt>emma</aunt>"
operator|+
literal|"<c:parent some_attr='2' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='c' anotherAttr='c'></c:child>"
operator|+
literal|"</c:parent></grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle/><aunt>emma</aunt>"
operator|+
literal|"<c:parent some_attr='2' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='d' anotherAttr='d'/>"
operator|+
literal|"</c:parent></grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle>ben</uncle><aunt/>"
operator|+
literal|"<c:parent some_attr='3' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='e' anotherAttr='e'></c:child>"
operator|+
literal|"</c:parent></grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle>ben</uncle><aunt/>"
operator|+
literal|"<c:parent some_attr='3' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='f' anotherAttr='f'/>"
operator|+
literal|"</c:parent></grandparent></g:greatgrandparent>"
block|}
decl_stmt|;
DECL|field|RESULTS_CHILD_MIXED
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_CHILD_MIXED
init|=
block|{
literal|"<child some_attr='a' anotherAttr='a' xmlns=\"urn:c\" xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"></child>"
block|,
literal|"<x:child xmlns:x='urn:c' some_attr='b' anotherAttr='b' xmlns='urn:c' xmlns:g='urn:g' xmlns:c='urn:c'/>"
block|,
literal|"<child some_attr='c' anotherAttr='c' xmlns='urn:c' xmlns:g='urn:g' xmlns:c='urn:c'></child>"
block|,
literal|"<c:child some_attr='d' anotherAttr='d' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
block|}
decl_stmt|;
DECL|field|RESULTS_CHILD_MIXED_JAVA8
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_CHILD_MIXED_JAVA8
init|=
block|{
literal|"<child some_attr='a' anotherAttr='a' xmlns=\"urn:c\" xmlns:c=\"urn:c\" xmlns:g=\"urn:g\"></child>"
block|,
literal|"<x:child xmlns:x='urn:c' some_attr='b' anotherAttr='b' xmlns='urn:c' xmlns:c='urn:c' xmlns:g='urn:g'/>"
block|,
literal|"<child some_attr='c' anotherAttr='c' xmlns='urn:c' xmlns:g='urn:g' xmlns:c='urn:c'></child>"
block|,
literal|"<c:child some_attr='d' anotherAttr='d' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
block|}
decl_stmt|;
DECL|field|RESULTS_CHILD_MIXED_WRAPPED
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_CHILD_MIXED_WRAPPED
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?><g:greatgrandparent xmlns:g='urn:g'><grandparent>"
operator|+
literal|"<parent some_attr='1' xmlns:c='urn:c' xmlns=\"urn:c\">"
operator|+
literal|"<child some_attr='a' anotherAttr='a'></child></parent></grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?><g:greatgrandparent xmlns:g='urn:g'><grandparent>"
operator|+
literal|"<parent some_attr='1' xmlns:c='urn:c' xmlns=\"urn:c\">"
operator|+
literal|"<x:child xmlns:x='urn:c' some_attr='b' anotherAttr='b'/></parent></grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?><g:greatgrandparent xmlns:g='urn:g'><grandparent>"
operator|+
literal|"<c:parent some_attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<child some_attr='c' anotherAttr='c' xmlns='urn:c'></child></c:parent></grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?><g:greatgrandparent xmlns:g='urn:g'><grandparent>"
operator|+
literal|"<c:parent some_attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<c:child some_attr='d' anotherAttr='d'/></c:parent></grandparent></g:greatgrandparent>"
block|}
decl_stmt|;
DECL|field|RESULTS_CHILD
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_CHILD
init|=
block|{
literal|"<c:child some_attr='a' anotherAttr='a' xmlns:g=\"urn:g\" xmlns:d=\"urn:d\" xmlns:c=\"urn:c\"></c:child>"
block|,
literal|"<c:child some_attr='b' anotherAttr='b' xmlns:g=\"urn:g\" xmlns:d=\"urn:d\" xmlns:c=\"urn:c\"/>"
block|,
literal|"<c:child some_attr='c' anotherAttr='c' xmlns:g=\"urn:g\" xmlns:d=\"urn:d\" xmlns:c=\"urn:c\"></c:child>"
block|,
literal|"<c:child some_attr='d' anotherAttr='d' xmlns:g=\"urn:g\" xmlns:d=\"urn:d\" xmlns:c=\"urn:c\"/>"
block|,
literal|"<c:child some_attr='e' anotherAttr='e' xmlns:g=\"urn:g\" xmlns:d=\"urn:d\" xmlns:c=\"urn:c\"></c:child>"
block|,
literal|"<c:child some_attr='f' anotherAttr='f' xmlns:g=\"urn:g\" xmlns:d=\"urn:d\" xmlns:c=\"urn:c\"/>"
block|}
decl_stmt|;
DECL|field|RESULTS_CHILD_JAVA8
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_CHILD_JAVA8
init|=
block|{
literal|"<c:child some_attr='a' anotherAttr='a' xmlns:c=\"urn:c\" xmlns:d=\"urn:d\" xmlns:g=\"urn:g\"></c:child>"
block|,
literal|"<c:child some_attr='b' anotherAttr='b' xmlns:c=\"urn:c\" xmlns:d=\"urn:d\" xmlns:g=\"urn:g\"/>"
block|,
literal|"<c:child some_attr='c' anotherAttr='c' xmlns:c=\"urn:c\" xmlns:d=\"urn:d\" xmlns:g=\"urn:g\"></c:child>"
block|,
literal|"<c:child some_attr='d' anotherAttr='d' xmlns:c=\"urn:c\" xmlns:d=\"urn:d\" xmlns:g=\"urn:g\"/>"
block|,
literal|"<c:child some_attr='e' anotherAttr='e' xmlns:c=\"urn:c\" xmlns:d=\"urn:d\" xmlns:g=\"urn:g\"></c:child>"
block|,
literal|"<c:child some_attr='f' anotherAttr='f' xmlns:c=\"urn:c\" xmlns:d=\"urn:d\" xmlns:g=\"urn:g\"/>"
block|}
decl_stmt|;
DECL|field|RESULTS_CHILD_NO_NS_MIXED
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_CHILD_NO_NS_MIXED
init|=
block|{
literal|"<child some_attr='a' anotherAttr='a' xmlns='' xmlns:g='urn:g' xmlns:c='urn:c'></child>"
block|,
literal|"<child some_attr='c' anotherAttr='c' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"></child>"
block|,     }
decl_stmt|;
DECL|field|RESULTS_CHILD_NO_NS_MIXED_JAVA8
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_CHILD_NO_NS_MIXED_JAVA8
init|=
block|{
literal|"<child some_attr='a' anotherAttr='a' xmlns='' xmlns:c='urn:c' xmlns:g='urn:g'></child>"
block|,
literal|"<child some_attr='c' anotherAttr='c' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"></child>"
block|,     }
decl_stmt|;
comment|// note that there is no preceding sibling to the extracted child
DECL|field|RESULTS_CHILD_NO_NS_MIXED_WRAPPED
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_CHILD_NO_NS_MIXED_WRAPPED
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?><g:greatgrandparent xmlns:g='urn:g'><grandparent>"
operator|+
literal|"<parent some_attr='1' xmlns:c='urn:c' xmlns=\"urn:c\">"
operator|+
literal|"<child some_attr='a' anotherAttr='a' xmlns=''></child></parent></grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?><g:greatgrandparent xmlns:g='urn:g'><grandparent>"
operator|+
literal|"<c:parent some_attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<child some_attr='c' anotherAttr='c'></child></c:parent></grandparent></g:greatgrandparent>"
block|,     }
decl_stmt|;
DECL|field|RESULTS_CHILD_NS_MIXED
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_CHILD_NS_MIXED
init|=
block|{
literal|"<x:child xmlns:x='urn:c' some_attr='b' anotherAttr='b' xmlns='urn:c' xmlns:g='urn:g' xmlns:c='urn:c'/>"
block|,
literal|"<c:child some_attr='d' anotherAttr='d' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
block|}
decl_stmt|;
DECL|field|RESULTS_CHILD_NS_MIXED_JAVA8
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_CHILD_NS_MIXED_JAVA8
init|=
block|{
literal|"<x:child xmlns:x='urn:c' some_attr='b' anotherAttr='b' xmlns='urn:c' xmlns:c='urn:c' xmlns:g='urn:g'/>"
block|,
literal|"<c:child some_attr='d' anotherAttr='d' xmlns:g=\"urn:g\" xmlns:c=\"urn:c\"/>"
block|}
decl_stmt|;
comment|// note that there is a preceding sibling to the extracted child
DECL|field|RESULTS_CHILD_NS_MIXED_WRAPPED
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_CHILD_NS_MIXED_WRAPPED
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?><g:greatgrandparent xmlns:g='urn:g'><grandparent>"
operator|+
literal|"<parent some_attr='1' xmlns:c='urn:c' xmlns=\"urn:c\">"
operator|+
literal|"<child some_attr='a' anotherAttr='a' xmlns=''></child>"
operator|+
literal|"<x:child xmlns:x='urn:c' some_attr='b' anotherAttr='b'/></parent></grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?><g:greatgrandparent xmlns:g='urn:g'><grandparent>"
operator|+
literal|"<c:parent some_attr='2' xmlns:c='urn:c'>"
operator|+
literal|"<child some_attr='c' anotherAttr='c'></child>"
operator|+
literal|"<c:child some_attr='d' anotherAttr='d'/></c:parent></grandparent></g:greatgrandparent>"
block|}
decl_stmt|;
DECL|field|RESULTS_PARENT_WRAPPED
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_PARENT_WRAPPED
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle/><aunt>emma</aunt>"
operator|+
literal|"<c:parent some_attr='1' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='a' anotherAttr='a'></c:child>"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b'/>"
operator|+
literal|"</c:parent></grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle/><aunt>emma</aunt>"
operator|+
literal|"<c:parent some_attr='2' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='c' anotherAttr='c'></c:child>"
operator|+
literal|"<c:child some_attr='d' anotherAttr='d'/>"
operator|+
literal|"</c:parent></grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle>ben</uncle><aunt/>"
operator|+
literal|"<c:parent some_attr='3' xmlns:c='urn:c' xmlns:d=\"urn:d\">"
operator|+
literal|"<c:child some_attr='e' anotherAttr='e'></c:child>"
operator|+
literal|"<c:child some_attr='f' anotherAttr='f'/>"
operator|+
literal|"</c:parent></grandparent></g:greatgrandparent>"
block|,     }
decl_stmt|;
DECL|field|RESULTS_PARENT
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_PARENT
init|=
block|{
literal|"<c:parent some_attr='1' xmlns:c='urn:c' xmlns:d=\"urn:d\" xmlns:g='urn:g'>"
operator|+
literal|"<c:child some_attr='a' anotherAttr='a'></c:child>"
operator|+
literal|"<c:child some_attr='b' anotherAttr='b'/>"
operator|+
literal|"</c:parent>"
block|,
literal|"<c:parent some_attr='2' xmlns:c='urn:c' xmlns:d=\"urn:d\" xmlns:g='urn:g'>"
operator|+
literal|"<c:child some_attr='c' anotherAttr='c'></c:child>"
operator|+
literal|"<c:child some_attr='d' anotherAttr='d'/>"
operator|+
literal|"</c:parent>"
block|,
literal|"<c:parent some_attr='3' xmlns:c='urn:c' xmlns:d=\"urn:d\" xmlns:g='urn:g'>"
operator|+
literal|"<c:child some_attr='e' anotherAttr='e'></c:child>"
operator|+
literal|"<c:child some_attr='f' anotherAttr='f'/>"
operator|+
literal|"</c:parent>"
block|,     }
decl_stmt|;
DECL|field|RESULTS_AUNT_WRAPPED
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_AUNT_WRAPPED
init|=
block|{
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle/><aunt>emma</aunt>"
operator|+
literal|"</grandparent></g:greatgrandparent>"
block|,
literal|"<?xml version='1.0' encoding='UTF-8'?>"
operator|+
literal|"<g:greatgrandparent xmlns:g='urn:g'><grandparent><uncle>ben</uncle><aunt/>"
operator|+
literal|"</grandparent></g:greatgrandparent>"
block|}
decl_stmt|;
DECL|field|RESULTS_AUNT
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_AUNT
init|=
block|{
literal|"<aunt xmlns:g=\"urn:g\">emma</aunt>"
block|,
literal|"<aunt xmlns:g=\"urn:g\"/>"
block|}
decl_stmt|;
DECL|field|RESULTS_AUNT_UNWRAPPED
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_AUNT_UNWRAPPED
init|=
block|{
literal|"emma"
block|,
literal|""
block|}
decl_stmt|;
DECL|field|RESULTS_GRANDPARENT_TEXT
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_GRANDPARENT_TEXT
init|=
block|{
literal|"emma"
block|,
literal|"ben"
block|}
decl_stmt|;
DECL|field|RESULTS_NULL
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|RESULTS_NULL
init|=
block|{     }
decl_stmt|;
DECL|field|nsmap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsmap
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|nsmap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|nsmap
operator|.
name|put
argument_list|(
literal|"G"
argument_list|,
literal|"urn:g"
argument_list|)
expr_stmt|;
name|nsmap
operator|.
name|put
argument_list|(
literal|"C"
argument_list|,
literal|"urn:c"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractChild ()
specifier|public
name|void
name|testExtractChild
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//C:child"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_CHILD_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractChildInjected ()
specifier|public
name|void
name|testExtractChildInjected
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|result
init|=
name|RESULTS_CHILD_JAVA8
decl_stmt|;
if|if
condition|(
name|isJava7OrLower
argument_list|()
condition|)
block|{
name|result
operator|=
name|RESULTS_CHILD
expr_stmt|;
block|}
name|invokeAndVerify
argument_list|(
literal|"//C:child"
argument_list|,
literal|'i'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractChildNSMixed ()
specifier|public
name|void
name|testExtractChildNSMixed
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//*:child"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY_NS_MIXED
argument_list|)
argument_list|,
name|RESULTS_CHILD_MIXED_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractChildNSMixedInjected ()
specifier|public
name|void
name|testExtractChildNSMixedInjected
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|result
init|=
name|RESULTS_CHILD_MIXED_JAVA8
decl_stmt|;
if|if
condition|(
name|isJava7OrLower
argument_list|()
condition|)
block|{
name|result
operator|=
name|RESULTS_CHILD_MIXED
expr_stmt|;
block|}
name|invokeAndVerify
argument_list|(
literal|"//*:child"
argument_list|,
literal|'i'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY_NS_MIXED
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractAnyChild ()
specifier|public
name|void
name|testExtractAnyChild
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//*:child"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_CHILD_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractCxxxd ()
specifier|public
name|void
name|testExtractCxxxd
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|result
init|=
name|RESULTS_CHILD_JAVA8
decl_stmt|;
if|if
condition|(
name|isJava7OrLower
argument_list|()
condition|)
block|{
name|result
operator|=
name|RESULTS_CHILD
expr_stmt|;
block|}
name|invokeAndVerify
argument_list|(
literal|"//C:c*d"
argument_list|,
literal|'i'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractUnqualifiedChild ()
specifier|public
name|void
name|testExtractUnqualifiedChild
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//child"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_NULL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractSomeUnqualifiedChild ()
specifier|public
name|void
name|testExtractSomeUnqualifiedChild
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//child"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY_NO_NS_MIXED
argument_list|)
argument_list|,
name|RESULTS_CHILD_NO_NS_MIXED_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractSomeUnqualifiedChildInjected ()
specifier|public
name|void
name|testExtractSomeUnqualifiedChildInjected
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|result
init|=
name|RESULTS_CHILD_NO_NS_MIXED_JAVA8
decl_stmt|;
if|if
condition|(
name|isJava7OrLower
argument_list|()
condition|)
block|{
name|result
operator|=
name|RESULTS_CHILD_NO_NS_MIXED
expr_stmt|;
block|}
name|invokeAndVerify
argument_list|(
literal|"//child"
argument_list|,
literal|'i'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY_NO_NS_MIXED
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractSomeQualifiedChild ()
specifier|public
name|void
name|testExtractSomeQualifiedChild
parameter_list|()
throws|throws
name|Exception
block|{
name|nsmap
operator|.
name|put
argument_list|(
literal|""
argument_list|,
literal|"urn:c"
argument_list|)
expr_stmt|;
name|invokeAndVerify
argument_list|(
literal|"//child"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY_NO_NS_MIXED
argument_list|)
argument_list|,
name|RESULTS_CHILD_NS_MIXED_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractSomeQualifiedChildInjected ()
specifier|public
name|void
name|testExtractSomeQualifiedChildInjected
parameter_list|()
throws|throws
name|Exception
block|{
name|nsmap
operator|.
name|put
argument_list|(
literal|""
argument_list|,
literal|"urn:c"
argument_list|)
expr_stmt|;
name|String
index|[]
name|result
init|=
name|RESULTS_CHILD_NS_MIXED_JAVA8
decl_stmt|;
if|if
condition|(
name|isJava7OrLower
argument_list|()
condition|)
block|{
name|result
operator|=
name|RESULTS_CHILD_NS_MIXED
expr_stmt|;
block|}
name|invokeAndVerify
argument_list|(
literal|"//child"
argument_list|,
literal|'i'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY_NO_NS_MIXED
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractWithNullNamespaceMap ()
specifier|public
name|void
name|testExtractWithNullNamespaceMap
parameter_list|()
throws|throws
name|Exception
block|{
name|nsmap
operator|=
literal|null
expr_stmt|;
name|String
index|[]
name|result
init|=
name|RESULTS_CHILD_NO_NS_MIXED_JAVA8
decl_stmt|;
if|if
condition|(
name|isJava7OrLower
argument_list|()
condition|)
block|{
name|result
operator|=
name|RESULTS_CHILD_NO_NS_MIXED
expr_stmt|;
block|}
name|invokeAndVerify
argument_list|(
literal|"//child"
argument_list|,
literal|'i'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY_NO_NS_MIXED
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractChildWithAncestorGGPdGP ()
specifier|public
name|void
name|testExtractChildWithAncestorGGPdGP
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"/G:greatgrandparent/grandparent//C:child"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_CHILD_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractChildWithAncestorGGPdP ()
specifier|public
name|void
name|testExtractChildWithAncestorGGPdP
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"/G:greatgrandparent//C:parent/C:child"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_CHILD_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractChildWithAncestorGPddP ()
specifier|public
name|void
name|testExtractChildWithAncestorGPddP
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//grandparent//C:parent/C:child"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_CHILD_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractChildWithAncestorGPdP ()
specifier|public
name|void
name|testExtractChildWithAncestorGPdP
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//grandparent/C:parent/C:child"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_CHILD_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractChildWithAncestorP ()
specifier|public
name|void
name|testExtractChildWithAncestorP
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//C:parent/C:child"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_CHILD_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractChildWithAncestorGGPdGPdP ()
specifier|public
name|void
name|testExtractChildWithAncestorGGPdGPdP
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"/G:greatgrandparent/grandparent/C:parent/C:child"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_CHILD_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractParent ()
specifier|public
name|void
name|testExtractParent
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//C:parent"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_PARENT_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractParentInjected ()
specifier|public
name|void
name|testExtractParentInjected
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//C:parent"
argument_list|,
literal|'i'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_PARENT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractAuntWC1 ()
specifier|public
name|void
name|testExtractAuntWC1
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//a*t"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_AUNT_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractAuntWC2 ()
specifier|public
name|void
name|testExtractAuntWC2
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//au?t"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_AUNT_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractAunt ()
specifier|public
name|void
name|testExtractAunt
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//aunt"
argument_list|,
literal|'w'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_AUNT_WRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractAuntInjected ()
specifier|public
name|void
name|testExtractAuntInjected
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//aunt"
argument_list|,
literal|'i'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_AUNT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractAuntUnwrapped ()
specifier|public
name|void
name|testExtractAuntUnwrapped
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//aunt"
argument_list|,
literal|'u'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_AUNT_UNWRAPPED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExtractGrandParentText ()
specifier|public
name|void
name|testExtractGrandParentText
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//grandparent"
argument_list|,
literal|'t'
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_GRANDPARENT_TEXT
argument_list|)
expr_stmt|;
block|}
DECL|method|invokeAndVerify (String path, char mode, InputStream in, String[] expected)
specifier|private
name|void
name|invokeAndVerify
parameter_list|(
name|String
name|path
parameter_list|,
name|char
name|mode
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|String
index|[]
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|XMLTokenExpressionIterator
name|xtei
init|=
operator|new
name|XMLTokenExpressionIterator
argument_list|(
name|path
argument_list|,
name|mode
argument_list|)
decl_stmt|;
name|xtei
operator|.
name|setNamespaces
argument_list|(
name|nsmap
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|xtei
operator|.
name|createIterator
argument_list|(
name|in
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|Closeable
operator|)
name|it
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"token count"
argument_list|,
name|expected
operator|.
name|length
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expected
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"mismatch ["
operator|+
name|i
operator|+
literal|"]"
argument_list|,
name|expected
index|[
name|i
index|]
argument_list|,
name|results
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isJava7OrLower ()
specifier|private
name|boolean
name|isJava7OrLower
parameter_list|()
block|{
return|return
name|getJavaMajorVersion
argument_list|()
operator|<=
literal|7
return|;
block|}
block|}
end_class

end_unit

