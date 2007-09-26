begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *   */
end_comment

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

begin_comment
comment|/**  * TODO Provide description for HttpGetWithQueryParamsTest.  *   * @author<a href="mailto:nsandhu@raleys.com">nsandhu</a>  *  */
end_comment

begin_class
DECL|class|HttpGetWithQueryParamsTest
specifier|public
class|class
name|HttpGetWithQueryParamsTest
extends|extends
name|HttpGetTest
block|{
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|expectedText
operator|=
literal|"activemq.apache.org"
expr_stmt|;
block|}
block|}
end_class

end_unit

