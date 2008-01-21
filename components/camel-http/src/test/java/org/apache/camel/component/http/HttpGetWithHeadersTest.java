begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
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
DECL|class|HttpGetWithHeadersTest
specifier|public
class|class
name|HttpGetWithHeadersTest
extends|extends
name|HttpGetTest
block|{
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"Content-Length"
argument_list|,
name|constant
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"Accept-Language"
argument_list|,
name|constant
argument_list|(
literal|"pl"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://www.google.com/search"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
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
comment|// "Szukaj" is "Search" in polish language
name|expectedText
operator|=
literal|"Szukaj"
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

