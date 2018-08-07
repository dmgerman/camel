begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_class
DECL|class|XsltUriResolverTest
specifier|public
class|class
name|XsltUriResolverTest
extends|extends
name|TestCase
block|{
DECL|method|testResolveUri ()
specifier|public
name|void
name|testResolveUri
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|XsltUriResolver
name|xsltUriResolver
init|=
operator|new
name|XsltUriResolver
argument_list|(
name|context
argument_list|,
literal|"classpath:xslt/staff/staff.xsl"
argument_list|)
decl_stmt|;
name|Source
name|source
init|=
name|xsltUriResolver
operator|.
name|resolve
argument_list|(
literal|"../../xslt/common/staff_template.xsl"
argument_list|,
literal|"classpath:xslt/staff/staff.xsl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"classpath:xslt/common/staff_template.xsl"
argument_list|,
name|source
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

