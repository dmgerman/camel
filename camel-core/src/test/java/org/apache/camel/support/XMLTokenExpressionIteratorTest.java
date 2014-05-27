begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *   */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

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
name|ExchangeBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|TestCase
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
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
name|exchange
operator|=
name|ExchangeBuilder
operator|.
name|anExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
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
literal|true
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
DECL|method|testExtractChildInjected ()
specifier|public
name|void
name|testExtractChildInjected
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//C:child"
argument_list|,
literal|false
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_BODY
argument_list|)
argument_list|,
name|RESULTS_CHILD
argument_list|)
expr_stmt|;
block|}
DECL|method|testExtractChildWithAncestorGGP_GP ()
specifier|public
name|void
name|testExtractChildWithAncestorGGP_GP
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"/G:greatgrandparent/grandparent//C:child"
argument_list|,
literal|true
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
DECL|method|testExtractChildWithAncestorGGP_P ()
specifier|public
name|void
name|testExtractChildWithAncestorGGP_P
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"/G:greatgrandparent//C:parent/C:child"
argument_list|,
literal|true
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
DECL|method|testExtractChildWithAncestorGP__P ()
specifier|public
name|void
name|testExtractChildWithAncestorGP__P
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//grandparent//C:parent/C:child"
argument_list|,
literal|true
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
DECL|method|testExtractChildWithAncestorGP_P ()
specifier|public
name|void
name|testExtractChildWithAncestorGP_P
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//grandparent/C:parent/C:child"
argument_list|,
literal|true
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
literal|true
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
DECL|method|testExtractChildWithAncestorGGP_GP_P ()
specifier|public
name|void
name|testExtractChildWithAncestorGGP_GP_P
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"/G:greatgrandparent/grandparent/C:parent/C:child"
argument_list|,
literal|true
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
DECL|method|textExtractParent ()
specifier|public
name|void
name|textExtractParent
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//C:parent"
argument_list|,
literal|true
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
DECL|method|textExtractParentInjected ()
specifier|public
name|void
name|textExtractParentInjected
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//C:parent"
argument_list|,
literal|false
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
DECL|method|textExtractAunt ()
specifier|public
name|void
name|textExtractAunt
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//aunt"
argument_list|,
literal|true
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
DECL|method|textExtractAuntInjected ()
specifier|public
name|void
name|textExtractAuntInjected
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeAndVerify
argument_list|(
literal|"//aunt"
argument_list|,
literal|false
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
DECL|method|invokeAndVerify (String path, boolean wrap, InputStream in, String[] expected)
specifier|private
name|void
name|invokeAndVerify
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|wrap
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
name|wrap
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
name|exchange
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
argument_list|<
name|String
argument_list|>
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
block|}
end_class

end_unit

